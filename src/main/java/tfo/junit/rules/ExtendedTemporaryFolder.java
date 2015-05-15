package tfo.junit.rules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>The ExtendedTemporaryFolder Rule allows creation of files and folders that should be deleted when the test method
 * finishes (whether it passes or fails). Whether the deletion is successful or not is not checked by this rule.</p>
 *
 * <p>This class offers a couple of features that the original JUnit implementation does not offer:</p>
 * <ul>
 *     <li>It is possible to specify a label string that is used as part of the temporary folder name. This is useful
 *         if tests use a number of rules of this type to distinguish which ExtendedTemporaryFolder is used for
 *         which purpose.
 *     </li>
 *     <li>On Windows: cleanup throws an exception if there are locked files in this temporary folder. This allows
 *         to detect unbalanced locking of files by the system under test.
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

    private boolean forceStrictDeletion = false;

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

    /**
     * <p>Causes this instance to check whether all files and folders can be deleted on
     * cleanup. If there are files that can't be deleted, an <code>IllegalStateException</code>
     * is thrown.</p>
     *
     * <p><strong>Please note:</strong> setting this flag to <code>true</code> has some downsides:</p>
     * <ol>
     *     <li>It only has any effect on Windows. It is possible to delete open files on POSIX based systems
     *         (i.e. OS X and Linux).
     *     </li>
     *     <li>The exceptions breaks the cleanup cycle of JUnit external resources. Any other resources used
     *         in the test where this method fails may not be properly cleaned up.
     *     </li>
     * </ol>
     *
     * <p>This flag is meant for Windows systems: files that can't be deleted after a test are still locked by
     * the system under test. Thus, there is some unbalanced locking going on, which might be a bug in the
     * system under test.</p>
     *
     * @param value the flag.
     */
    public void setForceStrictDeletion(boolean value) {
        this.forceStrictDeletion = value;
    }

    /**
     * Returns whether this instance throws if files can't be deleted on cleanup.
     *
     * @return the flag.
     */
    public boolean isForceStrictDeletion() {
        return forceStrictDeletion;
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

    /**
     * {@inheritDoc}
     *
     * This method throws an <code>IllegalStateException</code> if any of the content in this temporary folder can't
     * be deleted on cleanup.
     */
    @Override
    public void delete() {
        if (folder != null) {
            final Set<File> failures = new HashSet<>();
            recursiveDelete(folder, failures);
            if (forceStrictDeletion && !failures.isEmpty()) {
                throw new IllegalStateException(String.format("The following files could not be deleted: %s", failures.toString()));
            }
        }
    }

    private void recursiveDelete(@NotNull File file, @NotNull Set<File> failures) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File each : files) {
                recursiveDelete(each, failures);
            }
        }
        final boolean flag = file.delete();
        if (!flag) {
            failures.add(file);
        }
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
