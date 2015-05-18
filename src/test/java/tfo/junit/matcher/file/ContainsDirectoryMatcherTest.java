package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ContainsDirectoryMatcherTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private final ContainsDirectoryMatcher<File> subject = new ContainsDirectoryMatcher<>("dir");
    private final Description desc = new StringDescription();

    @Test
    public void directoryPresentShouldReturnTrue() throws IOException {
        folder.newFolder("dir");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void directoryAmongOthersShouldReturnTrue() throws IOException {
        folder.newFolder("folder");
        folder.newFolder("dir");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void missingDirShouldReturnFalse() {
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        final String expected = String.format("<%s> was an empty directory", folder.getRoot());
        assertThat(desc.toString(), equalTo(expected));
    }

    @Test
    public void missingButOtherFileShouldReturnFalse() throws IOException {
        folder.newFile("someOtherFile.txt");
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        final String expected = "found a directory containing the following: <someOtherFile.txt>";
        assertThat(desc.toString(), equalTo(expected));
    }

    @Test
    public void missingButOtherDirShouldReturnFalse() throws IOException {
        folder.newFolder("someDir");
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        final String expected = "found a directory containing the following: <someDir/>";
        assertThat(desc.toString(), equalTo(expected));
    }


}