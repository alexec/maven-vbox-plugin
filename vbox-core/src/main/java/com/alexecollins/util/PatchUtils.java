package com.alexecollins.util;

import name.fraser.neil.plaintext.diff_match_patch;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A facade/adapter.
 *
 * <code>
 *     diff -ruN a b > c
 * </code>
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class PatchUtils {

	private static final diff_match_patch P = new diff_match_patch();
	private static final Pattern H = Pattern.compile("(?:---|\\+\\+\\+) ([^\\t]*)\\t(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d\\d\\d\\d\\d\\d\\d (?:\\+|-)\\d\\d\\d\\d)");
	private static final SimpleDateFormat F = new SimpleDateFormat("yyyy-MM-dd DD:mm:ss.SSSSSSSSS ZZZZZ");
	private static final Date ZERO;

	static {
		assert H.matcher("--- a-file\t2013-01-27 15:45:00.000000000 +0000").find();
		assert H.matcher("--- /var/folders/tj/6fygy9t56p74dm95wc69mvm80000gn/T/1359306986738-0/a\t2013-01-27 27:16:26.000000000 +0000").find();
		final Matcher m = H.matcher("--- a\t1970-01-01 00:00:00.000000000 +0000");
		assert m.find();
		try {
			ZERO = F.parse("1970-01-01 00:00:00.000000000 +0000");
			final String group = m.group(2);
			System.out.println(group);
			System.out.println(F.parse(group).getTime());
			assert F.parse(group).getTime() == ZERO.getTime();
		} catch (ParseException e) {
			throw new AssertionError(e);
		}
	}

	private PatchUtils() {}

	public static void create(File a, File b, File patch) throws IOException {
		checkArgument(a != null && a.exists(), a + " exists");
		checkArgument(b != null && b.exists(), b + " exists");
		checkNotNull(patch, "patch is not null");
		final StringBuilder out = new StringBuilder();
		create(a, a, b, out);
		FileUtils.writeStringToFile(patch, out.toString(), "UTF-8");
	}

	private static void create(final File root, File a, File b, StringBuilder r) throws IOException {
		checkArgument(root.exists(), root + " must exist");

		// 2013-01-27 15:45:00.000000000 +0000
		final String path = root.toURI().relativize(a.toURI()).toString();
		r.append("--- ").append(path).append('\t').append(F.format(a.lastModified())).append('\n');
		r.append("+++ ").append(path).append('\t').append(F.format(b.lastModified())).append('\n');

		if (!a.exists()) {
			//System.out.println(a + " does not exist");
			return;
		}

		if (a.isFile()) {
			final String bText = b.exists() ? FileUtils.readFileToString(b, "UTF-8") : "";
			r.append(P.patch_toText(P.patch_make(FileUtils.readFileToString(a, "UTF-8"), bText)));
			//System.out.println("patch for file " + a);
		} else {
			//System.out.println("patch for dir " + a);
			final List<File> files = new ArrayList<File>(Arrays.asList(a.listFiles()));
			if (b.exists()) {
				for (File c : b.listFiles()){
					if (!new File(a, c.getName()).exists()) {
						files.add(new File(a, c.getName()));
					}
				}
			}
			Collections.sort(files);
			for (File c : files) {
				create(root, c, new File(b, c.getName()), r);
			}
		}
	}

	public static void apply(File a, File b, File patch) throws IOException, ParseException {
		checkArgument(a != null && a.exists(), b + " exists");
		checkNotNull(b, "b exists");
		checkArgument(patch != null && patch.isFile(), patch + " exists and is file");
		final FileInputStream in = new FileInputStream(patch);
		try {
			final BufferedReader p = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			apply(a, b, in, p);
		} finally {
			in.close();
		}
	}

	private static void apply(File a, File b, FileInputStream in, BufferedReader patch) throws IOException, ParseException {
		// date in header can be used to determine appearance/disappearance
		final String h0 = patch.readLine();
		if (h0 == null) {
			System.out.println("no more patch at " + a);
			return;
		}
		final String h1 = patch.readLine();

		final Matcher am = H.matcher(h0);
		if (!am.find()) {
			throw new IllegalStateException("header '" + h0 + "' invalid, must match " + H);
		}
		final Matcher bm = H.matcher(h0);
		if (!bm.find()) {
			throw new IllegalStateException("header '" + h1 + "' invalid, must match " + H);
		}

		final Date aMtime = F.parse(am.group(2));
		final Date bMtime = F.parse(bm.group(2));

		final StringBuilder p0 = new StringBuilder();
		while (true) {
			patch.mark((int) in.getChannel().position());
			final String l = patch.readLine();
			if (l == null || H.matcher(l).find()) {
				patch.reset();
				break;
			} else {
				p0.append(l).append('\n');
			}
		}

		 System.out.println(aMtime + " "  + bMtime + " "  + ZERO);

		// if mtime changes to 0, we delete the file
		final char op = aMtime.equals(bMtime) ? 'u' :
				bMtime.equals(ZERO) ? 'c' : aMtime.equals(ZERO) ? 'd' : '-';

		assert op != '-';


		System.out.println(a + " x " + op + " x " + p0);

		if (op == 'd') {
			if (b.isFile()) {
				assert b.delete();
			} else if (b.isDirectory()) {
				FileUtils.deleteDirectory(b);
			}
		}

		// TODO no support for changing a file into a directory
		if (a.isFile() || b.isFile()) {
			assert !a.isDirectory();
			assert !b.isDirectory();
			// TODO either a or b might be a directory
			final String aText = a.exists() ? FileUtils.readFileToString(a, "UTF-8") : "";
			final Object[] objects = P.patch_apply(P.patch_fromText(p0.toString()), aText);

			assert objects.length == 2;

			for (boolean b1 : (boolean[]) (objects[1])) {
				if (!b1) {
					throw new IllegalStateException("failed to apply patch " + p0.toString() + " to " + a);
				}
			}

			FileUtils.writeStringToFile(b, String.valueOf(objects[0]), "UTF-8");
		} else {
			// TODO either a or b might be a file
			assert !a.isFile();
			assert !b.isFile();

			if (!b.exists() && !b.mkdir()) {
				throw new IllegalStateException("failed to create " + b);
			}

			for (File c : a.listFiles()) {
				apply(c, new File(b, c.getName()), in, patch);
			}
		}
		if (b.exists() && !b.setLastModified(a.lastModified())) {
			throw new IllegalStateException("failed to set last modified date on " + b);
		}
	}
}
