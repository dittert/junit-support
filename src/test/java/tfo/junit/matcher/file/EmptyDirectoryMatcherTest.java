package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmptyDirectoryMatcherTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private final EmptyDirectoryMatcher subject = new EmptyDirectoryMatcher();
    private final Description desc = new StringDescription();

    @Test
    public void emptyDirectoryShouldWork() throws IOException {
        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void fileShouldNotBeEmpty() throws IOException {
        final File file = folder.newFile("test.txt");

        final boolean result = subject.matchesSafely(file, desc);

        assertThat(result, equalTo(false));
        final String expected = String.format("<%s> is not a directory.", file.getAbsolutePath());
        assertThat(desc.toString(), equalTo(expected));
    }

    @Test
    public void nonEmptyDirectoryShouldFail() throws IOException {
        final File file = folder.newFile("test.txt");

        final boolean result = subject.matchesSafely(folder.getRoot(), desc);

        assertThat(result, equalTo(false));
        final String expected = String.format("found a directory containing the following: <%s>", file.getAbsolutePath());
        assertThat(desc.toString(), equalTo(expected));
    }

}