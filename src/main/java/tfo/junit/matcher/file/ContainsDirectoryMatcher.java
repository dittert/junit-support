package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

final class ContainsDirectoryMatcher extends TypeSafeDiagnosingMatcher<File> {
	private final @NotNull String dir;

	ContainsDirectoryMatcher(@NotNull String dir) {
        this.dir = Objects.requireNonNull(dir);
    }

	@Override
	public boolean matchesSafely(@NotNull File item, @NotNull Description mismatchDescription) {
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
			mismatchDescription.appendText(" was an empty directory");
			return false;
		}
		for (File current : files) {
			if (current.getName().equals(dir) && current.isDirectory()) {
				return true;
			}
		}

		mismatchDescription.appendText("found a directory containing the following: ");
		ListHelper.appendFileList(files, mismatchDescription);
		return false;
	}

	@Override
	public void describeTo(@NotNull Description description) {
		description.appendText(String.format("a directory that contains a subdirectory named '%s'", dir));
	}
}
