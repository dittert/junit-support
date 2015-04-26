package tfo.matcher.file;


import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.io.File;

public final class DirectoryMatchers {

    /**
     * Prüfung, ob ein Verzeichnis eine Datei enthält.
     *
     * @param name der Name der Datei.
     * @return der Matcher.
     */
    @Factory
	public static Matcher<File> containsFile(String name) {
		return new ContainsFileMatcher(name);
	}

    /**
     * Prüfung, ob ein Verzeichnis ein Unterverzeichnis enthält.
     *
     * @param name der Name des Unterverzeichnisses.
     * @return der Matcher.
     * @see #missesDir(String)
     */
    @Factory
	public static Matcher<File> containsDir(String name) {
		return new ContainsDirectoryMatcher(name);
	}

    /**
     * Prüfung, ob ein Verzeichnis ein Unterverzeichnis <strong>nicht</strong> enthält.
     *
     * @param name der Name des Unterverzeichnisses.
     * @return der Matcher.
     */
    @Factory
	public static Matcher<File> missesDir(String name) {
		return new MissesDirectoryMatcher(name);
	}

    /**
     * Prüfung, ob ein Verzeichnis eine Datei <strong>nicht</strong> enthält.
     *
     * @param name der Name der Datie.
     * @return der Matcher.
     */
    @Factory
	public static Matcher<File> missesFile(String name) {
		return new MissesFileMatcher(name);
	}

    /**
     * Prüfung, ob ein Verzeichnis leer ist.
     *
     * @return der Matcher.
     */
    @Factory
	public static Matcher<File> isEmpty() {
		return new EmptyDirectoryMatcher();
	}
}
