package com.hp.com.hp.jenkins.plugins.alm.detritus;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/19/12
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SvnRepository {

    private SVNRepository localRepository;

    public SvnRepository(String path) throws SVNException {

        FSRepositoryFactory.setup();

        this.localRepository =
                SVNRepositoryFactory.create(
                        SVNRepositoryFactory.createLocalRepository(new File(path), true, false));
    }

    public void addToRepo(File dir, String repoPath, String commitMessage) throws SVNException, FileNotFoundException {

        final ISVNEditor commitEditor = this.localRepository.getCommitEditor(commitMessage, null, true, null);
        final SVNDeltaGenerator svnDeltaGenerator = new SVNDeltaGenerator();

        commitEditor.openRoot(-1);

        recursiveAddition(dir, repoPath, commitEditor, svnDeltaGenerator);

        commitEditor.closeDir();
        commitEditor.closeEdit();
    }

    private void recursiveAddition(File f, String repoPath, ISVNEditor commitEditor, SVNDeltaGenerator svnDeltaGenerator) throws SVNException, FileNotFoundException {

        if (f.isDirectory()) {

            commitEditor.addDir(repoPath, null, -1);

            for (File nestedFile : (f.listFiles())) {
                recursiveAddition(nestedFile, repoPath + "/" + nestedFile.getName(), commitEditor, svnDeltaGenerator);
            }

            commitEditor.closeDir();

        }

        else if (f.isFile()) {

            commitEditor.addFile(repoPath, null, -1);

            commitEditor.applyTextDelta(repoPath, null);
            final String checksum = svnDeltaGenerator.sendDelta(repoPath, new FileInputStream(f), commitEditor, true);

            commitEditor.closeFile(repoPath, checksum);
        }
    }

    public static void main(String args[]) throws SVNException, FileNotFoundException {

        //new SvnRepository("C:\\QC\\Views\\CI\\jenkins-plugins\\detritus\\src\\test\\resources\\repo")
        //        .addToRepo(new File("C:\\hofit\\mockProject"),"mockProject", "initial commit")
        //;
    }
}
