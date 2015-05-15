package tfo.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MissesDirectoryMatcherTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private final MissesDirectoryMatcher subject = new MissesDirectoryMatcher("test");
    private final Description desc = new StringDescription();

    @Test
    public void directoryMissingShouldReturnTrue() {
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void missingAmongOthersShouldReturnTrue() throws IOException {
        folder.newFolder("some other 1");
        folder.newFolder("some other 2");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void fileWithSameNameShouldReturnTrue() throws IOException {
        folder.newFile("test");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void existingDirShouldReturnFalse1() throws IOException {
        folder.newFolder("test");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("found a directory containing 1 entry: <test/>"));
    }

    @Test
    public void existingDirShouldReturnFalse2() throws IOException {
        folder.newFolder("test");
        folder.newFolder("some other");
        folder.newFile("test.txt");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("found a directory containing 3 entries: <some other/, test/, test.txt>"));
    }

    @Test
    public void missingBaseShouldReturnFalse() {

        final File missingBaseDir = new File(folder.getRoot(), "does not exist");
        final boolean result = subject.matchesSafely(missingBaseDir, desc);

        assertThat(result, equalTo(false));
        final String expected = String.format("<%s> does not exist.", missingBaseDir.getAbsolutePath());
        assertThat(desc.toString(), equalTo(expected));
    }

}