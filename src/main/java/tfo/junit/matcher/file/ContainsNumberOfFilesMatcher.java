package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static tfo.junit.matcher.file.Functions.toFile;

public class ContainsNumberOfFilesMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<? super Integer> numberMatcher;

    public ContainsNumberOfFilesMatcher(@NotNull Matcher<? super Integer> numberMatcher) {
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

        if (numberMatcher.matches(fileCount)) {
            return true;
        }

        mismatchDescription.appendText("a directory containing ");
        if (fileCount == 0) {
            mismatchDescription.appendText("no files");
        } else {
            mismatchDescription.appendValue(fileCount);
            mismatchDescription.appendText(" files");
        }
        if (dirCount == 0) {
            mismatchDescription.appendText(" and no directories");
        } else if (dirCount == 1) {
            mismatchDescription.appendText(" and one directory");
        } else {
            mismatchDescription.appendText(" and ");
            mismatchDescription.appendValue(dirCount);
            mismatchDescription.appendText(" directories");
        }
        return false;
    }

    @Override
    public void describeTo(@NotNull Description description) {
        description.appendText("a directory with the number of files ");
        final StringDescription tmp = new StringDescription();
        numberMatcher.describeTo(tmp);
        if (tmp.toString().startsWith("a value ")) {
            // yeah, well: a directory containing a value greater than <3> files doesn't sound to good in Englisch...
            description.appendText(tmp.toString().substring(8));
        } else {
            description.appendText(tmp.toString());
        }
    }
}
