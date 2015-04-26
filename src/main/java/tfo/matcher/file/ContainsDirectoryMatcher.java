package tfo.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.File;

final class ContainsDirectoryMatcher extends TypeSafeDiagnosingMatcher<File> {
	private final String dir;

	ContainsDirectoryMatcher(String dir) {
		this.dir = dir;
	}

	@Override
	public boolean matchesSafely(File item, Description mismatchDescription) {
		if (!item.exists()) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendText(" does not exist.");
			return false;
		}

		if (!item.isDirectory()) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendText(" is not a directory.");
			return false;
		}

		final File[] files = item.listFiles();
		if (files == null || files.length == 0) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendValue(" was an empty directory");
			return false;
		}
		for (File current : files) {
			if (current.getName().equals(dir) && current.isDirectory()) {
				return true;
			}
		}

		mismatchDescription.appendText("found a directory containing the following: ");
		for (int i = 0; i < files.length; i++) {
			final File file = files[i];
			mismatchDescription.appendText(file.getName());
			if (i < files.length) {
				mismatchDescription.appendText(", ");
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("a directory that contains the subdirectory '%s'", dir));
	}
}
