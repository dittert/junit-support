package tfo.junit.matcher.file;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static tfo.junit.matcher.file.Functions.toFile;

final class EmptyDirectoryMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

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
        if (files == null) {
            mismatchDescription.appendValue(item);
            mismatchDescription.appendText(" did not return any files on listFiles()");
            return false;
        } else if (files.length != 0) {
            mismatchDescription.appendText("found a directory containing the following: ");
            mismatchDescription.appendValueList("", ", ", "", files);
            return false;
        }

        return true;
    }

	@Override
	public void describeTo(@NotNull Description description) {
		description.appendText("an empty directory.");
	}
}
