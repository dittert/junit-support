package tfo.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListHelperTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final Description description = new StringDescription();

    @Test
    public void singleFileShouldBeListed() throws IOException {
        folder.newFile("file.txt");

        ListHelper.appendFileList(contents(), description);

        assertThat(description.toString(), equalTo("<file.txt>"));
    }

    @Test
    public void multipleFilesShouldBeListedInOrder() throws IOException {
        folder.newFile("a.txt");
        folder.newFile("b.txt");
        folder.newFile("c.txt");

        final File[] contents = {
                new File(folder.getRoot(), "c.txt"),
                new File(folder.getRoot(), "b.txt"),
                new File(folder.getRoot(), "a.txt")
        };
        ListHelper.appendFileList(contents, description);

        assertThat(description.toString(), equalTo("<a.txt, b.txt, c.txt>"));
    }

    @Test
    public void singleDirectoryShouldBeListed() throws IOException {
        folder.newFolder("dir");

        ListHelper.appendFileList(contents(), description);

        assertThat(description.toString(), equalTo("<dir/>"));
    }

    @Test
    public void multipleFilesAndFoldersShouldBeListedInOrder() throws IOException {
        folder.newFile("a.txt");
        folder.newFolder("b");
        folder.newFile("c.txt");

        final File[] contents = {
                new File(folder.getRoot(), "c.txt"),
                new File(folder.getRoot(), "b"),
                new File(folder.getRoot(), "a.txt")
        };
        ListHelper.appendFileList(contents, description);

        assertThat(description.toString(), equalTo("<a.txt, b/, c.txt>"));
    }

    @NotNull
    private File[] contents() {
        final File[] result = folder.getRoot().listFiles();
        if (result == null) {
            throw new IllegalStateException();
        }
        return result;
    }


}