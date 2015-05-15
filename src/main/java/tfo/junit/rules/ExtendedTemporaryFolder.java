package tfo.junit.rules;

import org.jetbrains.annotations.Nullable;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * <p>The ExtendedTemporaryFolder Rule allows creation of files and folders that should
 * be deleted when the test method finishes (whether it passes or
 * fails). Whether the deletion is successful or not is not checked by this rule.</p>
 *
 * <p>This class offers a couple of features that the original JUnit implementation
 * does not offer:</p>
 * <ul>
 *     <li>It is possible to specify a label string that is used as part of the
 *         temporary folder name. This is useful if tests use a number of rules of
 *         this type to distinguish which ExtendedTemporaryFolder is used for
 *         which purpose.
 *     </li>
 * </ul>
 */
public class ExtendedTemporaryFolder extends TemporaryFolder {

    @Nullable
    private String label;

    @Nullable
    private final File parentFolder;

    @Nullable
    private File folder;

    public ExtendedTemporaryFolder() {
        this(null, null);
    }

    public ExtendedTemporaryFolder(@Nullable File parentFolder) {
        this(parentFolder, null);
    }

    public ExtendedTemporaryFolder(@Nullable String label) {
        this(null, label);
    }

    public ExtendedTemporaryFolder(@Nullable File parentFolder, @Nullable String label) {
        super(parentFolder);
        this.label = label;
        this.parentFolder = parentFolder;
    }

    @Override
    public void create() throws IOException {
        folder = createTemporaryFolderIn(parentFolder, label);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createTemporaryFolderIn(@Nullable File parentFolder, @Nullable String label) throws IOException {
        final String prefix;
        if (label == null) {
            prefix = "junit";
        } else {
            final String tmpLabel = (label.length() > 5) ? label.substring(0, 6) : label;
            prefix = "junit-" + tmpLabel + "-";
        }
        File createdFolder = File.createTempFile(prefix, "", parentFolder);
        createdFolder.delete();
        createdFolder.mkdir();
        return createdFolder;
    }

    @Override
    public void delete() {
        if (folder != null) {
            recursiveDelete(folder);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void recursiveDelete(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File each : files) {
                recursiveDelete(each);
            }
        }
        file.delete();
    }

    /**
     * @return the location of this temporary folder.
     */
    public File getRoot() {
        if (folder == null) {
            throw new IllegalStateException("the temporary folder has not yet been created");
        }
        return folder;
    }

}
