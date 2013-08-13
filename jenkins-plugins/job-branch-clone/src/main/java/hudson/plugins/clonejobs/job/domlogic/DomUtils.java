package hudson.plugins.clonejobs.job.domlogic;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.string.StringUtils;
import hudson.model.Job;
import hudson.plugins.clonejobs.CloneJobsAction;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/05/13
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class DomUtils {

    public static final OutputFormat PRETTY_PRINT = OutputFormat.createPrettyPrint();

    //reader used to transform xml of original jobs to dom
    public static SAXReader saxReader = new SAXReader();

    public static void findAndReplaceInNodeValues(Element replaceRoot, Map<String, String> jobTargets) {

        updateValue(replaceRoot, jobTargets);

        updateAttributes(replaceRoot, jobTargets);

        for (Element el : (Collection<Element>)replaceRoot.elements()) {
            findAndReplaceInNodeValues(el, jobTargets);
        }
    }

    private static void updateAttributes(Element replaceRoot, Map<String, String> jobTargets) {

        for (Attribute at : (Collection<Attribute>)replaceRoot.attributes()) {

            String stringValue = at.getText();

            if (stringValue != null && !stringValue.isEmpty()) {
                stringValue = replaceMappingInString(jobTargets, stringValue);

                at.setText(stringValue);
            }
        }
    }

    private static String replaceMappingInString(Map<String, String> jobTargets, String stringValue) {

        final ArrayList<String> froms = new ArrayList<String>();
        final ArrayList<String> tos = new ArrayList<String>();

        CollectionUtils.map(
                jobTargets.entrySet(),
                new Handler<Void, Map.Entry<String, String>>() {

                    @Override
                    public Void apply(Map.Entry<String, String> o) {
                        froms.add(o.getKey());
                        tos.add(o.getValue());
                        return null;
                    }
                }
        );

        final String[] fromArray = froms.toArray(new String[froms.size()]);
        final String[] toArray = tos.toArray(new String[tos.size()]);

        return org.apache.commons.lang.StringUtils.replaceEach(
                stringValue,
                fromArray,
                toArray);
    }

    public static void updateValue(Element replaceRoot, Map<String, String> jobTargets) {

        String stringValue = replaceRoot.getText();

        if (stringValue != null && !stringValue.isEmpty()) {

            stringValue = replaceMappingInString(jobTargets, stringValue);

            replaceRoot.setText(stringValue);
        }
    }

    public static void findOccurrences(Element searchRoot, Collection<String> searchedFor, Collection<String> found) {

        final String value = searchRoot.getText();
        StringUtils.findStringsInString(searchedFor, found, value);

        for (Attribute attribute : (Collection<Attribute>)searchRoot.attributes()) {
            StringUtils.findStringsInString(searchedFor, found, attribute.getText());
        }

        for (Element element : (Collection<Element>)searchRoot.elements()) {
            findOccurrences(element, searchedFor, found);
        }
    }

    public static String docToString(Document doc) {
        final StringWriter writer = new StringWriter();
        XMLWriter logMessageWriter = new XMLWriter(writer, PRETTY_PRINT);
        try { logMessageWriter.write(doc); } catch (IOException neverHappen) {}
        return writer.toString();
    }

    public static Document jobToDom(Job job) {

        try {
            final Document read = saxReader.read(job.getConfigFile().getFile());
            return read;
        }
        catch (DocumentException e) {

            final String errorString = StringUtils.exceptionToString(e);
            CloneJobsAction.LOGGER.warning("failed to clone " + job.getFullName() + ". " + errorString);
            return null;
        }
    }
}
