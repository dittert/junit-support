package tfo.junit.matcher.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

final class Functions {

    private Functions() {
        // no instances
    }

    @NotNull
    public static File toFile(@NotNull Object o) {
        if (o instanceof File) {
            return (File) o;
        } else if (o instanceof Path) {
            return ((Path) o).toFile();
        } else {
            throw new IllegalArgumentException(String.format("Can't convert '%s' to file. Expected java.io.File or java.nio.file.Path but got %s.", o, o.getClass().getName()));
        }
    }
}
