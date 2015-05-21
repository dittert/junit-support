package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContainsNumberOfDirectoriesMatcherTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final ContainsNumberOfDirectoriesMatcher<File> subject = new ContainsNumberOfDirectoriesMatcher<>(equalTo(2));
    private final Description desc = new StringDescription();


    @Test
    public void twoDirectoriesShouldBeOk() throws Exception {
        folder.newFolder("dir1");
        folder.newFolder("dir2");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void wrongNumberOfDirectoriesShouldReturnFalse() throws Exception {
        folder.newFolder("dir1");
        folder.newFolder("dir2");
        folder.newFolder("dir3");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing <3> directories and no files"));
    }

    @Test
    public void noDirectoryShouldReturnFalse() throws Exception {
        final File file = folder.newFile();

        final boolean result = subject.matchesSafely(file, desc);
        final String expected = String.format("<%s> is not a directory.", file.getAbsolutePath());

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo(expected));
    }

    @Test
    public void missingFileShouldReturnFalse() throws  Exception {
        final File missing = new File(folder.getRoot(), "missing");

        final boolean result = subject.matchesSafely(missing, desc);
        final String expected = String.format("<%s> does not exist.", missing.getAbsolutePath());

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo(expected));
    }

    @Test
    public void nothingShouldReturnFalse() throws Exception {
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing no directories and no files"));
    }

    @Test
    public void justFileShouldReturnFalse() throws Exception {
        folder.newFile();
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing no directories and one file"));
    }

    @Test
    public void multipleFilesShouldReturnFalse() throws Exception {
        folder.newFile();
        folder.newFile();
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing no directories and <2> files"));
    }

}