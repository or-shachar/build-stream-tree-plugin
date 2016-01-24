package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.Util;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 01:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class StringProvider implements Describable<StringProvider> {


    public StringProvider() {}

    public StringProviderDescriptor getDescriptor() {
        return (StringProviderDescriptor)Jenkins.getInstance().getDescriptor(getClass());
    }

    public static abstract class StringProviderDescriptor extends Descriptor<StringProvider> implements ExtensionPoint {

        protected StringProviderDescriptor(Class<? extends StringProvider> clazz) {
            super(clazz);
        }

        protected StringProviderDescriptor() {
        }
    }

    public static List<? extends Descriptor<? extends StringProvider>> all() {
        return Jenkins.getInstance().<StringProvider, StringProviderDescriptor>getDescriptorList(StringProvider.class);
    }

/* **************************************************
* IMPLEMENTATIONS!
***************************************************/


    public static class TextareaStringProvider extends StringProvider {

        private String stringToProvide;

        public String getStringToProvide() {
            return stringToProvide;
        }

        public void setStringToProvide(String stringToProvide) {
            this.stringToProvide = stringToProvide;
        }

        @DataBoundConstructor
        public TextareaStringProvider(String stringToProvide) {
            this.stringToProvide = stringToProvide;
        }

        @Override
        public String toString() {
            return stringToProvide;
        }

        @Extension
        public static class DescriptorImpl extends StringProviderDescriptor {

            @Override
            public String getDisplayName() {
                return "Text Area";
            }
        }
    }

    public static class NoneStringProvider extends StringProvider {

        @DataBoundConstructor
        public NoneStringProvider() {}

        @Override
        public String toString() {
            return null;
        }

        @Extension(ordinal = Integer.MAX_VALUE)
        public static class DescriptorImpl extends StringProviderDescriptor {

            @Override
            public String getDisplayName() {
                return "None";
            }
        }
    }

    public static class DefaultStringProvider extends StringProvider {

        private static final String DEFAULTS_PATH = DefaultStringProvider.class.getName().replaceAll("[\\$\\.]","/") + "/defaults/";

        private String selectedDefaultName;
        private transient String selectedDeafultText;

        @DataBoundConstructor
        public DefaultStringProvider(String selectedDefaultName) {
            this.selectedDefaultName = selectedDefaultName;
        }

        public String getSelectedDefaultName() {
            return selectedDefaultName;
        }

        public void setSelectedDefaultName(String selectedDefaultName) {
            this.selectedDefaultName = selectedDefaultName;
        }

        public static Collection<String> getDefaultsList() throws IOException {
            Collection ret = new ArrayList();
            Class clazz = DefaultStringProvider.class;
            URL dirURL = clazz.getClassLoader().getResource(DEFAULTS_PATH);

            if (dirURL.getProtocol().equals("jar")) {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Log.debug("DOWNSTREAM-LOGS: getting resource for default list from " + jar.getName());
                Enumeration entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String name = ((JarEntry)entries.nextElement()).getName();
                    if (!name.equals(DEFAULTS_PATH))
                    {
                        if (name.startsWith(DEFAULTS_PATH)) {
                            String entry = name.substring(DEFAULTS_PATH.length());
                            if (!entry.equals("")) {
                                int checkSubdir = entry.indexOf("/");
                                if (checkSubdir < 0)
                                {
                                    ret.add(entry);
                                    Log.debug("DOWNSTREAM-LOGS: found resource for default list: " + entry);
                                }
                            }
                        }
                    }
                }
                return ret;
            }

            throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
        }

        public static String getDefaultText(String defaultName) {
            final String path = DEFAULTS_PATH + defaultName;
            String text;
            try {
                text = new Scanner(DefaultStringProvider.class.getClassLoader().getResourceAsStream(path)).useDelimiter("\\Z").next();
            }
            catch (Exception e) {
                text = e.getMessage();
                Log.throwing("DefaultStringProvider", "getDefaultText", e);
            }
            return text;
        }

        public String getSelectedDeafultText() {

            if (selectedDeafultText == null) {
                selectedDeafultText = getDefaultText(selectedDefaultName);
            }

            return selectedDeafultText;
        }

        public void setSelectedDeafultText(String selectedDeafultText) {
            this.selectedDeafultText = selectedDeafultText;
        }

        @Override
        public String toString() {

            return getSelectedDeafultText();
        }

        @Extension
        public static class DescriptorImpl extends StringProviderDescriptor {

            @Override
            public String getDisplayName() {
                return "Default Configuration";
            }
        }
    }

    public static class FileStringProvider extends StringProvider {

        private String stringToProvide;

        public String getStringToProvide() {
            return stringToProvide;
        }

        public void setStringToProvide(String stringToProvide) {
            this.stringToProvide = stringToProvide;
        }

        @DataBoundConstructor
        public FileStringProvider(String pathToFile) {

            try {
                this.stringToProvide = Util.loadFile(new File(pathToFile));
            }

            catch (IOException e) {
                Log.warning("couldn't read file " + pathToFile);
                Log.throwing(FileStringProvider.class.getName(), "FileStringProvider", e);
                stringToProvide = "failed to read file " + pathToFile;
            }
        }

        @Override
        public String toString() {
            return stringToProvide;
        }

        @Extension
        public static class DescriptorImpl extends StringProviderDescriptor {

            @Override
            public String getDisplayName() {
                return "File";
            }
        }
    }

}
