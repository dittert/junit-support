package tfo.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.File;

final class ContainsFileMatcher extends TypeSafeDiagnosingMatcher<File> {
	private final String file;

	ContainsFileMatcher(String file) {
		this.file = file;
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
			if (current.getName().equals(file) && current.isFile()) {
				return true;
			}
		}

		mismatchDescription.appendText("found a directory containing the following: ");
		mismatchDescription.appendValueList("", ", ", "", files);
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(String.format("a directory that contains the file '%s'", file));
	}
}
