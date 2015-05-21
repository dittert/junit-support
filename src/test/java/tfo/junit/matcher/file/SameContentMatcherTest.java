package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SameContentMatcherTest {

    private static final String CONTENT = "abc";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private SameContentMatcher<File> subject;
    private final Description desc = new StringDescription();
    private File reference;

    @Before
    public void setup() throws IOException {
        reference = folder.newFile("reference");
        Files.write(reference.toPath(), CONTENT.getBytes(), StandardOpenOption.CREATE);
        subject = new SameContentMatcher<>(reference);
    }

    @Test
    public void sameFileShouldReturnTrue() throws Exception {
        final boolean result = subject.matchesSafely(reference, desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void equalFileShouldReturnTrue() throws Exception {
        final boolean result = subject.matchesSafely(new File(reference.getAbsolutePath()), desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void sameContentShouldReturnTrue() throws Exception {
        final File other = folder.newFile();
        Files.write(other.toPath(), CONTENT.getBytes(), StandardOpenOption.CREATE);

        final boolean result = subject.matchesSafely(other, desc);

        assertThat(result, equalTo(true));
        assertThat(desc.toString(), equalTo(""));
    }

    @Test
    public void differentContentShouldReturnFalse() throws Exception {
        final File other = folder.newFile();
        Files.write(other.toPath(), "some other stuff".getBytes(), StandardOpenOption.CREATE);

        final boolean result = subject.matchesSafely(other, desc);

        assertThat(result, equalTo(false));
        assertThat(desc.toString(), equalTo("found two different files"));
    }

}