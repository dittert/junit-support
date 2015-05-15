package tfo.junit.rules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static tfo.junit.matcher.file.DirectoryMatchers.isEmpty;

public class ExtendedTemporaryFolderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    // There's no unit test whether an exception is thrown if some files could not be
    // deleted: our CI/development is on POSIX systems and it's possible to delete
    // files there. It's only Windows that doesn't like deleting locked files...


    @Test
    public void folderNameShouldContainLabel() throws Exception {
        final ExtendedTemporaryFolder subject = new ExtendedTemporaryFolder("label");
        try {
            subject.create();
            final String dirName = subject.getRoot().getName();

            assertThat(dirName, containsString("label"));
        } finally {
            subject.delete();
        }
    }

    @Test
    public void folderShouldBeDeletedAfterwards() throws Exception {
        final ExtendedTemporaryFolder subject = new ExtendedTemporaryFolder(folder.getRoot(), "label");
        try {
            subject.create();
        } finally {
            subject.delete();
        }

        assertThat(folder.getRoot(), isEmpty());
    }

}