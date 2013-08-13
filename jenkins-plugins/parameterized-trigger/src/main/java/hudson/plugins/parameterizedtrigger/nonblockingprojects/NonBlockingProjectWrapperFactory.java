package hudson.plugins.parameterizedtrigger.nonblockingprojects;


import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import hudson.Util;
import hudson.model.*;
import jenkins.model.Jenkins;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/7/12
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class NonBlockingProjectWrapperFactory {

    public static <T extends AbstractProject> T unblock(T project) {

        return (T)ClassImposterizer.INSTANCE.imposterise(
                new JobDelegationInterceptor(project),
                project.getClass(),
                Queue.NonBlockingTask.class);
    }


    private static class JobDelegationInterceptor implements MethodInterceptor {

        /*
        it's conceivable that this object might somehow be serialized.
         we only store the project name so that we don't accidentally serialize (and potentially unserialize)
         a duplicate instance for the project - which is supposed to be a kind of singleton...
         */
        private String projectName;

        public JobDelegationInterceptor(AbstractProject project) {
            projectName = project.getName();
        }

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            //we don't want to delegate the scheduling - that will schedule the internal non enhanced project..
            if (method.getName().equals("scheduleBuild2")) {

                if (objects[2] instanceof Collection) {
                    return this.scheduleBuild2(
                        (AbstractProject)o,
                        (Integer)objects[0],
                        (Cause)objects[1],
                        (Collection)objects[2]);
                }
                else {
                    return this.scheduleBuild2(
                        (AbstractProject)o,
                        (Integer)objects[0],
                        (Cause)objects[1],
                        (Action[])objects[2]);
                }
            }

            if (method.getName().equals("getSubTasks")) {
                return this.repairGetSubTasks(delegated(), o, ((AbstractProject)delegated()).getSubTasks());
            }

            else {
                return method.invoke(
                        delegated(),
                        objects
                );
            }
        }

        private AbstractProject delegated() {
            return (AbstractProject)Jenkins.getInstance().getItemByFullName(projectName);
        }

        private Object repairGetSubTasks(final AbstractProject delegated, final Object overridingThis, List subTasks) {
            return CollectionUtils.map(subTasks, new Handler() {
                @Override
                public Object apply(Object node) {
                    return node.equals(delegated) ? overridingThis : node;
                }
            });
        }

        public Future scheduleBuild2(AbstractProject overridingThis, int quietPeriod, Cause c, Action... actions) {
            return this.scheduleBuild2(overridingThis, quietPeriod, c, Arrays.asList(actions));
        }

        /**
         * augmented method for scheduling a build, but supports overriding what is actually scheduled
         */
        @SuppressWarnings("unchecked")
        public Future scheduleBuild2(AbstractProject overridingThis, int quietPeriod, Cause c, Collection<? extends Action> actions) {
            if (!overridingThis.isBuildable())
                return null;

            List<Action> queueActions = new ArrayList<Action>(actions);
            if (overridingThis.isParameterized() && Util.filter(queueActions, ParametersAction.class).isEmpty()) {

                List defaultParametersValues = null;
                try {
                    defaultParametersValues =
                            (List)AbstractProject.class.getMethod("getDefaultParametersValues").invoke(overridingThis);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

                queueActions.add(new ParametersAction(defaultParametersValues));
            }

            if (c != null) {
                queueActions.add(new CauseAction(c));
            }

            Queue.WaitingItem i = Jenkins.getInstance().getQueue().schedule(overridingThis, quietPeriod, queueActions);
            if(i!=null)
                return (Future)i.getFuture();
            return null;
        }
    }

}
