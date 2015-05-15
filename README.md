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