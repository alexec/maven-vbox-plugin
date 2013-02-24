package com.alexecollins.vbox.core.task;

import com.alexecollins.util.FileUtils2;
import com.alexecollins.vbox.core.VBox;
import com.alexecollins.vbox.core.Work;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTask implements Callable<Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);
	protected final Work work;
	protected final VBox box;

	protected AbstractTask(Work work, VBox box) {
		this.work = work;
		this.box = box;
	}


	void verifySignature() throws Exception {
		if (box.exists()) {
			final byte[] sig = getSignature();
			final File sigFile = getSignatureFile();

			if (!sigFile.exists() || !Arrays.equals(FileUtils.readFileToByteArray(sigFile), sig)) {
				LOGGER.info(box.getName() + " signature has changed, and therefore the source files have probably changed");

				new Clean(work ,box).call();
			}
		}
	}

	File getSignatureFile() {
		return new File(work.targetOf(box), "signature");
	}

	byte[] getSignature() throws NoSuchAlgorithmException, IOException {
		return FileUtils2.getSignature(new File(box.getSrc().toURL().getFile()));
	}

	/**
	 *  Complete the variables.
	 */
	public String subst(String line) throws IOException, InterruptedException, ExecutionException {
		line = line.replaceAll("\\$\\{vbox\\.additions\\}", VBox.findGuestAdditions().getPath().replaceAll("\\\\", "/"));
		line = line.replaceAll("\\$\\{vbox\\.name\\}", box.getName());
		return line;
	}
}
