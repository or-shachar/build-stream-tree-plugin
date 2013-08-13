package com.hp.maven.plugins.compress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

/**
 * @goal gzip
 */
public class ZipMojo extends AbstractMojo {

	/**
	 * The Directory that contains the files to be zipped.
	 * 
	 * @parameter
	 * @required
	 */
	private File basedir;

	/**
	 * specifies the file extensions that will be zipped. if not set - all files will be zipped
	 * 
	 * @parameter
	 */
	private String[] extensions;

	/**
	 * whether to write detailed log messages
	 * 
	 * @parameter default-value=false
	 */
	private boolean detailedLog;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (basedir == null || !basedir.exists()) {
			getLog().error("Can't run plugin. basedir does not exist");
		}
		else {
			if (detailedLog) {
				getLog().info("Detailed log is ON");
			}
			else {
				getLog().info("Detailed log is OFF");
			}

			getLog().info("Starting zip plugin on basedir: " + basedir.toString());

			if (extensions == null) {
				getLog().warn("Extensions are not set. Zipping all files.");
			}
			else {
				getLog().info("Extensions to zip: " + StringUtils.join(extensions, ";"));
			}

			Collection<File> files = FileUtils.listFiles(basedir, extensions, true);

			getLog().info("starting to zip: " + files.size() + " files");

			for (File file : files) {
				compressFile(file.getPath());
			}

			getLog().info("Done zip");
		}

	}

	private void compressFile(String inFileName) {
		try {
			File file = new File(inFileName);
			logInDetail(" going to gzip the  : " + file + " file");
			FileOutputStream fos = new FileOutputStream(file + ".gz");
			logInDetail(" the name of this gzip file is  : " + file + ".gz");
			GZIPOutputStream gzos = new GZIPOutputStream(fos);

			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			logInDetail(" transferring file from" + inFileName + " to " + file + ".gz");
			byte[] buffer = new byte[1024];
			int i;
			while ((i = in.read(buffer)) >= 0) {
				gzos.write(buffer, 0, i);
			}
			logInDetail(" file is in now gzip format");
			in.close();
			gzos.close();
		}
		catch (IOException e) {
			getLog().error("Exception is" + e);
		}
	}

	// used for more detailed log messages. writes to log only if detailedLog is set to true
	private void logInDetail(String message) {
		if (detailedLog) {
			getLog().info(message);
		}
	}

}
