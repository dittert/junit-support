package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static tfo.junit.matcher.file.Functions.toFile;

final class ContainsNumberOfDirectoriesMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<? super Integer> numberMatcher;

    public ContainsNumberOfDirectoriesMatcher(@NotNull Matcher<? super Integer> numberMatcher) {
        this.numberMatcher = Objects.requireNonNull(numberMatcher);
    }

    @Override
    protected boolean matchesSafely(@NotNull T item, @NotNull Description mismatchDescription) {
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
        }

        int fileCount = 0;
        int dirCount = 0;
        for (File file : files) {
            if (file.isFile()) {
                fileCount++;
            } else if (file.isDirectory()) {
                dirCount++;
            }
        }

        if (numberMatcher.matches(dirCount)) {
            return true;
        }

        mismatchDescription.appendText("a directory containing ");
        if (dirCount == 0) {
            mismatchDescription.appendText("no directories");
        } else {
            mismatchDescription.appendValue(dirCount);
            mismatchDescription.appendText(" directories");
        }
        if (fileCount == 0) {
            mismatchDescription.appendText(" and no files");
        } else if (fileCount == 1) {
            mismatchDescription.appendText(" and one file");
        } else {
            mismatchDescription.appendText(" and ");
            mismatchDescription.appendValue(fileCount);
            mismatchDescription.appendText(" files");
        }
        return false;
    }

    @Override
    public void describeTo(@NotNull Description description) {
        description.appendText("a directory with the number of directories ");
        final StringDescription tmp = new StringDescription();
        numberMatcher.describeTo(tmp);
        if (tmp.toString().startsWith("a value ")) {
            // yeah, well: a directory containing a value greater than <3> directories doesn't sound to good in Englisch...
            description.appendText(tmp.toString().substring(8));
        } else {
            description.appendText(tmp.toString());
        }
    }
}
