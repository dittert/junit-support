package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContainsNumberOfFilesMatcherTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final ContainsNumberOfFilesMatcher<File> subject = new ContainsNumberOfFilesMatcher<>(equalTo(2));
    private final Description desc = new StringDescription();


    @Test
    public void twoFilesShouldBeOk() throws Exception {
        folder.newFile("file1");
        folder.newFile("file2");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void wrongNumberOfFilesShouldReturnFalse() throws Exception {
        folder.newFile("file1");
        folder.newFile("file2");
        folder.newFile("file3");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing <3> files and no directories"));
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
        assertThat(desc.toString(), equalTo("a directory containing no files and no directories"));
    }

    @Test
    public void justADirectoryShouldReturnFalse() throws Exception {
        folder.newFolder();
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing no files and one directory"));
    }

    @Test
    public void multipleDirectoriesShouldReturnFalse() throws Exception {
        folder.newFolder();
        folder.newFolder();
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("a directory containing no files and <2> directories"));
    }

}