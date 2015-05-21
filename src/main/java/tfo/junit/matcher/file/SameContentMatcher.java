package tfo.junit.matcher.file;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

import static tfo.junit.matcher.file.Functions.toFile;

final class SameContentMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private static final int EOF = -1;

    private final @NotNull File reference;

    public SameContentMatcher(@NotNull T reference) {
        Objects.requireNonNull(reference);

        this.reference = toFile(reference);

        if (!this.reference.isFile()) {
            throw new IllegalArgumentException(String.format("'%s' is no file.", this.reference.getAbsolutePath()));
        }
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        final File file = toFile(item);
        if (reference.equals(file)) {
            return true;
        }

        try {
            if (contentEquals(reference, file)) {
                return true;
            }

            mismatchDescription.appendText("found two different files");
            return false;
        } catch (IOException e) {
            mismatchDescription.appendText(e.getMessage());
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a file with the same content as ");
        description.appendValue(reference);
    }

    // borrowed from commons-io FileUtils and IOUtils
    private static boolean contentEquals(@NotNull final File file1, @NotNull final File file2) throws IOException {
        final boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }

        if (!file1Exists) {
            // two not existing files are equal
            return true;
        }

        if (file1.isDirectory() || file2.isDirectory()) {
            // don't want to compare directory contents
            throw new IOException("Can't compare directories, only files");
        }

        if (file1.length() != file2.length()) {
            // lengths differ, cannot be equal
            return false;
        }

        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            // same file
            return true;
        }

        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            return contentEquals(input1, input2);

        } finally {

            closeQuietly(input1);
            closeQuietly(input2);
        }
    }

    private static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

    private static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (EOF != ch) {
            final int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        final int ch2 = input2.read();
        return ch2 == EOF;
    }
}
