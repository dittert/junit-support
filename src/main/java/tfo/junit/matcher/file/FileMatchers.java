package tfo.junit.matcher.file;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public final class FileMatchers {

    private FileMatchers() {
        // no instances
    }

    /**
     * Checks whether the file has the same content as the given file.
     *
     * @param reference the file to compare to.
     * @return the mather.
     */
    @Factory
    @NotNull
    public static Matcher<File> sameFileAs(@NotNull File reference) {
        return new SameContentMatcher<>(reference);
    }

    /**
     * Checks whether the file has the same content as the given file.
     *
     * @param reference the file to compare to.
     * @return the mather.
     */
    @Factory
    @NotNull
    public static Matcher<Path> sameFileByPathAs(@NotNull Path reference) {
        return new SameContentMatcher<>(reference);
    }
}
