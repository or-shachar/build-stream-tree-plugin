package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.plugins.emailext.plugins.ContentBuilder;
import hudson.plugins.emailext.plugins.EmailToken;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.content.ScriptContentBuildWrapper;
import hudson.tasks.Mailer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.Scanner;

import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

@EmailToken
public class DownstreamLogsNestedEmailContent extends DataBoundTokenMacro {

    public static final String MACRO_NAME = "TOKENIZED_JELLY_SCRIPT";
    private static final String DEFAULT_HTML_TEMPLATE_NAME = "html";
    private static final String DEFAULT_TEXT_TEMPLATE_NAME = "text";
    private static final String DEFAULT_TEMPLATE_NAME = DEFAULT_HTML_TEMPLATE_NAME;
    private static final String EMAIL_TEMPLATES_DIRECTORY = "email-templates";

    @Parameter
    public String template = DEFAULT_TEMPLATE_NAME;

    @Override
    public boolean acceptsMacroName(String macroName) {
        return macroName.equals(MACRO_NAME);
    }
    /*
     public String getHelpText() {
     return "Custom message content generated from a Jelly script template. "
     + "There are two templates provided: \"" + DEFAULT_HTML_TEMPLATE_NAME + "\" "
     + "and \"" + DEFAULT_TEXT_TEMPLATE_NAME + "\". Custom Jelly templates should be placed in "
     + "$JENKINS_HOME/" + EMAIL_TEMPLATES_DIRECTORY + ". When using custom templates, "
     + "the template filename without \".jelly\" should be used for "
     + "the \"" + TEMPLATE_NAME_ARG + "\" argument.\n"
     + "<ul>\n"
     + "<li><i>" + TEMPLATE_NAME_ARG + "</i> - the template name.<br>\n"
     + "Defaults to \"" + DEFAULT_TEMPLATE_NAME + "\".\n"
     + "</ul>\n";
     }
     */

    @Override
    public String evaluate(AbstractBuild<?, ?> build, TaskListener listener, String macroName)
            throws MacroEvaluationException, IOException, InterruptedException {
        InputStream inputStream = null;

        try {
            inputStream = getTemplateInputStream(template);
            return renderContent(build, inputStream, listener);
        } catch (JellyException e) {
            //LOGGER.log(Level.SEVERE, null, e);
            return "JellyException: " + e.getMessage();
        } catch (FileNotFoundException e) {
            String missingTemplateError = generateMissingTemplate(template);
            //LOGGER.log(Level.SEVERE, missingTemplateError);
            return missingTemplateError;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private String generateMissingTemplate(String template) {
        return "Jelly script [" + template + "] was not found in $JENKINS_HOME/" + EMAIL_TEMPLATES_DIRECTORY + ".";
    }

    /**
     * Try to get the template from the classpath first before trying the file
     * system.
     *
     * @param templateName
     * @return
     * @throws java.io.FileNotFoundException
     */
    private InputStream getTemplateInputStream(String templateName)
            throws FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                "hudson/plugins/emailext/templates/" + templateName + ".jelly");

        if (inputStream == null) {
            final File templatesFolder = new File(Hudson.getInstance().getRootDir(), EMAIL_TEMPLATES_DIRECTORY);
            final File templateFile = new File(templatesFolder, templateName + ".jelly");
            inputStream = new FileInputStream(templateFile);
        }

        return inputStream;
    }

    private String renderContent(AbstractBuild<?, ?> build, InputStream inputStream, TaskListener listener)
            throws JellyException, IOException, InterruptedException, MacroEvaluationException {
        JellyContext context = createContext(new ScriptContentBuildWrapper(build), build);

        //nathang: do embedding of nested tokens
        String unenhancedText = new Scanner(inputStream).useDelimiter("\\Z").next();
        String newText = TokenMacro.expandAll(build, listener, unenhancedText, false, ContentBuilder.getPrivateMacros());

        Script script = context.compileScript(new InputSource(new ByteArrayInputStream(newText.getBytes())));
        if (script != null) {
            return convert(context, script);
        }
        return null;
    }

    private String convert(JellyContext context, Script script)
            throws JellyTagException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(16 * 1024);
        XMLOutput xmlOutput = XMLOutput.createXMLOutput(output);
        script.run(context, xmlOutput);
        xmlOutput.flush();
        xmlOutput.close();
        output.close();
        return output.toString(getCharset());
    }

    private JellyContext createContext(Object it, AbstractBuild<?, ?> build) {
        JellyContext context = new JellyContext();
        context.setVariable("it", it);
        context.setVariable("build", build);
        context.setVariable("project", build.getParent());
        context.setVariable("rooturl", new ExtendedEmailPublisher().getDescriptor().getHudsonUrl());
        return context;
    }

    private String getCharset() {
        String charset = Mailer.descriptor().getCharset();
        String overrideCharset = new ExtendedEmailPublisher().getDescriptor().getCharset();
        if (overrideCharset != null) {
            charset = overrideCharset;
        }
        return charset;
    }
}
