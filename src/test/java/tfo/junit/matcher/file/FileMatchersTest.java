package tfo.junit.matcher.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileMatchersTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test(expected = IllegalArgumentException.class)
    public void sameFileShouldThrowIfNoFileWasGiven() throws Exception {
        new SameContentMatcher<>(folder.getRoot());
    }

}