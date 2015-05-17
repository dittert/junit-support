# JUnit Support
A library to support unit testing with [JUnit](https://github.com/junit-team/junit) and
[Hamcrest](https://github.com/hamcrest/JavaHamcrest). This library is designed to require minimal external dependencies.

## Download
[ ![Download](https://api.bintray.com/packages/dittert/maven/tfo.junit-support/images/download.svg) ](https://bintray.com/dittert/maven/tfo.junit-support/_latestVersion)

This library is available on Bintray. Use the following snippet to include it in your `build.gradle` file:

    repositories {
        maven {
            url  "http://dl.bintray.com/dittert/maven" 
        }
    }

    // ...

    dependencies {
        testCompile group: 'tfo', name: 'junit-support', version: '0.1'
    }

IDEA currently seems to have problems indexing Bintray repositories that were created through Gradle. If you're using IDEA, please feel free to vote for [IDEA-138029](https://youtrack.jetbrains.com/issue/IDEA-138029). 

**Please note:** This does not affect the build itself, code completion in IDEA just doesn't suggest any artifacts from this repository. 

Once this library is more mature, we'll publish it to the `jCenter()` repository.

## A note about Files and Paths
Oracle introduced a new class `java.nio.file.Path` to handle paths in a platform agnostic way with JDK 7. This causes a number of challenges if you're writing real world unit tests: In these, you often end up dealing with instances of both types which then require matchers with the same functionality for both types.

Even though there is an easy way to convert between both types (`toFile()` on path objects and `toPath()` on file objects) that isn't sufficient for unit tests. This conversion is clumsy, if you don't know whether a value is null or not. Suppose that there's a `pathHolder` object that might return `null` from `get()`:

    assertThat(pathHolder.get().toFile(), containsFile("readme.txt"));

Your IDE will flag the call to `toFile()` as potential null pointer access. Fixing this requires a bit of boiler plate code which obscures the concept of the original text.

This leaves us in a bit of a bind: Hamcrest uses static helper methods to provide the developer with the corresponding type of matcher. But Java doesn't allow overloaded methods that differ only in the return type. Thus, the only way to solve this problem is to distinguish methods by name. As a consequence, there are the following two methods in the API:

    public Matcher<File> DirectoryMatchers.containsFile(String name);
    public Matcher<Path> DirectoryMatchers.containsFileByPath(String name);

All provider methods operating on paths have `ByPath` appended to their name.