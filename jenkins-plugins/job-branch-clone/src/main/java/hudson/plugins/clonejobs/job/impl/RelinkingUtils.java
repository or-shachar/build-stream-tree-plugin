package hudson.plugins.clonejobs.job.impl;

import hudson.model.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 12:23 AM
 * To change this template use File | Settings | File Templates.
 *
 * utility class that helps with common relinking operations.
 */
public class RelinkingUtils {

    /**
     *
     * TODO: move to collectionUtils in commons and rename to "replaceListElementsUsingReplacementMapping"
     *
     * @param mapping the mapping used for replacing keys with their associated values
     * @param elements list of elements that may contain originals we wish to replace with clones
     * @param <T> type of the list
     * @return a newly allocated list which is a copy of the elements input list, except that any instance
     * that exists as a key in mapping is replaced with it's value.
     */
    public static <T> List<T> transferList(Map<? super T, ? super T> mapping, List<T> elements) {

        final List<T> replacements = new ArrayList<T>(elements.size());

        for (T currentlyLinked: elements) {

            final boolean hasReplacement = mapping.containsKey(currentlyLinked);

            replacements.add(hasReplacement ?
                    (T)mapping.get(currentlyLinked) :
                    currentlyLinked);
        }

        return replacements;
    }

    /**
     * @param original2cloned the project original->cloned mapping
     * @return a matching mappnig bu instead of project instances, use their names as Strings.
     */
    public static Map<String, String> calcProjectNameMapping(Map<? extends Job, ? extends Job> original2cloned) {

        Map<String, String> projectNameMapping = new HashMap<String,String>(original2cloned.size());

        for (Map.Entry<? extends Job, ? extends Job> projectClonePair : original2cloned.entrySet()) {
            projectNameMapping.put(projectClonePair.getKey().getName(), projectClonePair.getValue().getName());
        }

        return projectNameMapping;
    }
}
