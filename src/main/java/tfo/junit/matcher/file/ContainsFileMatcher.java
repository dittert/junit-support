package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static tfo.junit.matcher.file.Functions.toFile;

final class ContainsFileMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
	private final @NotNull String file;

	ContainsFileMatcher(@NotNull String file) {
        this.file = Objects.requireNonNull(file);
    }

	@Override
	public boolean matchesSafely(@NotNull T item, @NotNull Description mismatchDescription) {
        final File f = toFile(item);

		if (!f.exists()) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendText(" does not exist.");
			return false;
		}

		if (!f.isDirectory()) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendText(" is not a directory.");
			return false;
		}

		final File[] files = f.listFiles();
		if (files == null || files.length == 0) {
			mismatchDescription.appendValue(item);
			mismatchDescription.appendText(" was an empty directory");
			return false;
		}
		for (File current : files) {
			if (current.getName().equals(file) && current.isFile()) {
				return true;
			}
		}

		mismatchDescription.appendText("found a directory containing the following: ");
		ListHelper.appendFileList(files, mismatchDescription);
		return false;
	}

	@Override
	public void describeTo(@NotNull Description description) {
		description.appendText(String.format("a directory that contains a file named '%s'", file));
	}
}
