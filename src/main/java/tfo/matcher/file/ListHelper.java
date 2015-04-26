package tfo.matcher.file;

import org.hamcrest.Description;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

final class ListHelper {

    private ListHelper() {
        // no instances.
    }

    static void appendFileList(@NotNull File[] files, @NotNull Description description) {
        Arrays.sort(files);

        description.appendText("<");
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            description.appendText(file.getName());
            if (file.isDirectory()) {
                description.appendText("/");
            }

            if (i < files.length - 1) {
                description.appendText(", ");
            }
        }
        description.appendText(">");
    }
}
