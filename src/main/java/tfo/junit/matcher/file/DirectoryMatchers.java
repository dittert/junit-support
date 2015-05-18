package tfo.junit.matcher.file;


import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.equalTo;

@SuppressWarnings("unused") // API methods
public final class DirectoryMatchers {

    private DirectoryMatchers() {
        // no instances.
    }

    /**
     * Checks whether a directory contains a file.
     *
     * @param name the name of the file.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<File> containsFile(@NotNull String name) {
		return new ContainsFileMatcher<>(name);
	}

    /**
     * Checks whether a directory contains a file.
     *
     * @param name the name of the file.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<Path> containsFileByPath(@NotNull String name) {
		return new ContainsFileMatcher<>(name);
	}

    @Factory
    @NotNull
    public static Matcher<File> containsNumberOfFiles(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("There can't be less than zero files. But you expected " + number);
        }
        final ContainsNumberOfFilesMatcher<File> result = new ContainsNumberOfFilesMatcher<>(equalTo(number));
        if (number == 0) {
            return describedAs("a directory with no files", result);
        }
        return result;
    }

    @Factory
    @NotNull
    public static Matcher<File> containsNumberOfFiles(Matcher<? super Integer> matcher) {
        Objects.requireNonNull(matcher);
        // there's no further error checking here, because there's no way to know if got something useful passed into
        // this method.
        return new ContainsNumberOfFilesMatcher<>(matcher);
    }

    /**
     * Checks whether a directory contains a subdirectory.
     *
     * @param name the name of the subdirectory.
     * @return the matcher.
     * @see #missesDir(String)
     */
    @Factory
    @NotNull
	public static Matcher<File> containsDir(@NotNull String name) {
		return new ContainsDirectoryMatcher<>(name);
	}

    /**
     * Checks whether a directory contains a subdirectory.
     *
     * @param name the name of the subdirectory.
     * @return the matcher.
     * @see #missesDir(String)
     */
    @Factory
    @NotNull
	public static Matcher<Path> containsDirByPath(@NotNull String name) {
		return new ContainsDirectoryMatcher<>(name);
	}

    /**
     * Checks whether a directory does <strong>not</strong> contain a subdirectory.
     *
     * @param name the name of the subdirectory.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<File> missesDir(@NotNull String name) {
		return new MissesDirectoryMatcher<>(name);
	}

    /**
     * Checks whether a directory does <strong>not</strong> contain a subdirectory.
     *
     * @param name the name of the subdirectory.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<Path> missesDirByPath(@NotNull String name) {
		return new MissesDirectoryMatcher<>(name);
	}

    /**
     * Checks whether a directory does <strong>not</strong> contain a file.
     *
     * @param name the name of the file.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<File> missesFile(@NotNull String name) {
		return new MissesFileMatcher<>(name);
	}

    /**
     * Checks whether a directory does <strong>not</strong> contain a file.
     *
     * @param name the name of the file.
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<Path> missesFileByPath(@NotNull String name) {
		return new MissesFileMatcher<>(name);
	}

    /**
     * Checks whether a directory is empty.
     *
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<File> isEmpty() {
		return new EmptyDirectoryMatcher<>();
	}

    /**
     * Checks whether a directory is empty.
     *
     * @return the matcher.
     */
    @Factory
    @NotNull
	public static Matcher<Path> isEmptyByPath() {
		return new EmptyDirectoryMatcher<>();
	}
}
