package tfo.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

final class MissesFileMatcher extends TypeSafeDiagnosingMatcher<File> {
	private final @NotNull String name;

	MissesFileMatcher(@NotNull String name) {
        this.name = Objects.requireNonNull(name);
    }

	@Override
	protected boolean matchesSafely(@NotNull File item, @NotNull Description mismatchDescription) {
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
			return true;
		}

		for (File current : files) {
			if (current.isFile() && current.getName().equals(name)) {
				mismatchDescription.appendText("found a directory containing ");
				if (files.length == 1) {
					mismatchDescription.appendText("1 entry: ");
				} else {
					mismatchDescription.appendText(String.valueOf(files.length));
					mismatchDescription.appendText(" entries: ");
				}
				for (int i = 0; i < files.length; i++) {
					final File file = files[i];
					mismatchDescription.appendText(file.getName());
					if (i < files.length) {
						mismatchDescription.appendText(", ");
					}
				}
				return false;
			}
		}

		return true;
	}

	@Override
	public void describeTo(@NotNull Description description) {
		description.appendText(String.format("a directory that does not contain file '%s'", name));
	}
}
