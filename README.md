# TODO

# Getting Started

## Requirements

* Java 18 - preferred way of installation is [SDKMAN!](https://sdkman.io/)

>The project is based on Java 18, because it's the latest Java version supported by
[Gradle](https://docs.gradle.org/current/userguide/compatibility.html) at the moment.

## How to build
```bash
./gradlew clean check
```

## How to run
```bash
./gradlew bootRun
```

# Notes

## Git

I usually do not keep commit history in such high granulation on the master branch. Instead, during a merge to the master,
I squash all commits from the feature branch (preferably short living) so that there is only one commit on the master branch
that represents all the changes.

## Tests

Human-friendly test names should work in IntelliJ IDEA by default, but in the past, the following setting was needed:
> Build, Execution, Deployment -> Build Tools -> Gradle
> * Run tests using: **IntelliJ IDE**

The effect should be as the following:

![Human readable displayed test names](docs/images/human_readable_displayed_test_names.png)

## Null-Safety

I use an annotation `@NonNullApi` on a package level to instruct IDE that my code is non-nullable,
so that IDE can detect and shows warnings about nullability (static analysis). Additionally, I use Lombok `@NonNull`
to get a guaranty that I will not receive any null values. If I need to return a value which can be null:

* if it is a public API then I return an `Optional`.
* otherwise I can declare it with `@Nullable` annotation, so that IDE will not rise a warning.

This way I can get much better protection from NPE in a Java. The whole idea is described here:
[Spring Null-Safety Annotations](https://www.baeldung.com/spring-null-safety-annotations)

# Tech Stack

## Frameworks

* [Spring Reactive](https://spring.io/reactive) - a pet project like this is an excellent opportunity to try new things!  

## Libraries

* [vavr](https://www.vavr.io/) - turns java™ upside down
* [JUnit 5](https://junit.org/junit5/) - programmer-friendly testing framework for Java and the JVM
* [AssertJ](https://assertj.github.io/doc/) - fluent assertions java library

## Tools

* [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
* [Project Lombok](https://projectlombok.org/)
* [SpotBugs](https://spotbugs.github.io/) - a program which uses static analysis to look for bugs in Java code.
