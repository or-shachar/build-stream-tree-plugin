package hudson.plugins.clonejobs.view.impl;

import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import static jenkins.model.Jenkins.checkGoodName;


/**
 * Created with IntelliJ IDEA.
 * User: elimeleh
 * Date: 21/10/12
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 *
 * this class handles ajax methods for validation or job names views and other use inputs.
 * these functions are directly accessible from jenkins, but we wrap them in ajax to make use of
 * jenkins framework of ajax proxy in js.
 *
 * TODO this class should be outside the view.impl package, directly in clonejobs
 */
public class JsProxyValidationHandler {

    @JavaScriptMethod
    /**
     * @param jobName - the candidate name for a job being validated
     * @return null if ok, string of error message otherwise.

     * a wrapper for the jenkins checkJobName (e.g. http://localhost:8080/checkJobName?value={jobName}
     * uses jenkins validation that jobName is ok (i.e unused and contains no bad characters)
     */
    public String validateJobName(String jobName) throws FormValidation {

        final FormValidation formValidation = Jenkins.getInstance().doCheckJobName(jobName);

        return (formValidation == FormValidation.ok()) ?
            "ok" :
            formValidation.getMessage();
    }


    /**
     *
     * @param viewName the view name candidate being validated
     * @return string with error message, or null if no error
     *
     * a wrapper for the jenkins viewExistsCheck (e.g. http://localhost:8080/viewExistsCheck?value={viewName}
     * uses jenkins validation that viewName is ok (i.e unused and contains no bad characters)
     */
    @JavaScriptMethod
    public String validateViewName(String viewName) throws FormValidation {

        final FormValidation formValidation = Jenkins.getInstance().doViewExistsCheck(viewName);
        //TODO ternary operator: return formValidation!=FormValidation.ok ? formValidation.getMessage : null;
        try{
            checkGoodName(viewName);

        }catch (Exception invalidViewNameExp){
            return invalidViewNameExp.getMessage();
        };
        if (formValidation != FormValidation.ok()) {
            //throw formValidation;
            return formValidation.getMessage();
        }
        return null;
    }


}
