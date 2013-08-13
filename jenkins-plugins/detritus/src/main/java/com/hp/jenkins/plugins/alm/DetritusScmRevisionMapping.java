package com.hp.jenkins.plugins.alm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusScmRevisionMapping {

    Map<DetritusBuildWrapper, BuildScmMapping> map =
            new HashMap<DetritusBuildWrapper, BuildScmMapping>();

    public void add(DetritusBuildWrapper build, Map<String, Long> firstRevisions, Map<String, Long> lastRevisions) {

        map.put(build, new BuildScmMapping(firstRevisions, lastRevisions));
    }

    @Override
    public String toString() {
        return "DetritusScmRevisionMapping{" +
                "map=" + map +
                '}';
    }

    public class BuildScmMapping {

        Map<String, RevisionInterval> revisions;

        public BuildScmMapping(Map<String, Long> firstRevisions, Map<String, Long> lastRevisions) {

            logWarnings(firstRevisions, lastRevisions);

            revisions = new HashMap<String, RevisionInterval>(lastRevisions.size());

            final Set<Map.Entry<String, Long>> entries = lastRevisions.entrySet();
            for (Map.Entry<String,Long> entry : entries) {

                final String repositoryPath = entry.getKey();

                final Long lastRevision = entry.getValue();
                final Long firstRevision = firstRevisions.get(repositoryPath);

                revisions.put(repositoryPath, new RevisionInterval(firstRevision, lastRevision));
            }
        }

        @Override
        public String toString() {
            return "" + revisions;
        }

        private void logWarnings(Map<String, Long> firstRevisionsMap, Map<String, Long> lastRevisionsMap) {

            final Set<String> firstRevisions = firstRevisionsMap.keySet();
            final Set<String> lastRevisions = lastRevisionsMap.keySet();

            //TODO: add code to handle this case, and find the latest known revision of current repositories.
            if (!firstRevisions.equals(lastRevisions)) {

                DetritusListener.getInstance().error("current SCM repository structure doesn't match the one used " +
                        "when the tests passed. " +
                        "\npaths that exist now and didn't when the tests passed: " +
                        new HashSet(lastRevisions).removeAll(firstRevisions)+ ".");
            }
        }

    }


    private class RevisionInterval {

        private Long firstRevision;
        private Long lastRevision;

        public RevisionInterval(Long firstRevision, Long lastRevision) {

            this.firstRevision = firstRevision;
            this.lastRevision = lastRevision;
        }

        public Long getFirstRevision() {
            return firstRevision;
        }

        public Long getLastRevision() {
            return lastRevision;
        }

        @Override
        public String toString() {
            return "[" +
                    "" + firstRevision +
                    ", " + lastRevision +
                    ']';
        }
    }
}
