package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class DirectoryMatchersTest {

    private final Description desc = new StringDescription();


    @Test
    public void numberOfFilesShouldBeReadable() {
        final Matcher<File> subject = DirectoryMatchers.containsNumberOfFiles(greaterThanOrEqualTo(3));

        subject.describeTo(desc);

        assertThat(desc.toString(), equalTo("a directory with the number of files equal to or greater than <3>"));
    }

    @Test
    public void numberOfFilesShouldBeOptimized() {
        final Matcher<File> subject = DirectoryMatchers.containsNumberOfFiles(0);

        subject.describeTo(desc);

        assertThat(desc.toString(), equalTo("a directory with no files"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeNumberShouldThrowForFiles() {
        DirectoryMatchers.containsNumberOfFiles(-1);
    }

    @Test
    public void numberOfDirectoriesShouldBeReadable() {
        final Matcher<File> subject = DirectoryMatchers.containsNumberOfDirectories(greaterThanOrEqualTo(3));

        subject.describeTo(desc);

        assertThat(desc.toString(), equalTo("a directory with the number of directories equal to or greater than <3>"));
    }

    @Test
    public void numberOfDirectoriesShouldBeOptimized() {
        final Matcher<File> subject = DirectoryMatchers.containsNumberOfDirectories(0);

        subject.describeTo(desc);

        assertThat(desc.toString(), equalTo("a directory with no subdirectories"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeNumberShouldThrowForDirs() {
        DirectoryMatchers.containsNumberOfDirectories(-1);
    }

}