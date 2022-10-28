package pl.szymsoft.junit;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.removeEnd;

class HumanReadableDisplayNameGenerator extends DisplayNameGenerator.ReplaceUnderscores {

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceDollarWithDot(formatTestTypeName(super.generateDisplayNameForClass(testClass)));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceDollarWithDot(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return replaceDollarWithDot(super.generateDisplayNameForMethod(testClass, testMethod));
    }

    private static String replaceDollarWithDot(String name) {
        return name.replace('$', '.');
    }

    /**
     * With usage of <a href="https://openjdk.java.net/jeps/420">JEP 420: Pattern Matching for switch (Second Preview)</a>
     * it could be implemented as following:
     * <p>
     * {@snippet :
     * import static org.apache.commons.lang3.StringUtils.removeEnd;
     * 
     * return switch (name) {
     *    case String aString && aString.endsWith("UnitTest") ->
     *        removeEnd(aString, "UnitTest") + " Unit Test";
     *    case String aString && aString.endsWith("IntegrationTest") ->
     *        removeEnd(aString, "IntegrationTest") + " Integration Test";
     *    default -> name;
     * };
     *}
     * </p>
     */
    private static String formatTestTypeName(String name) {

        return Stream.of(
                        new Replacement("UnitTest", "Unit Test"),
                        new Replacement("IntegrationTest", "Integration Test"))
                .filter(pair -> name.endsWith(pair.ending))
                .findFirst()
                .map(pair -> removeEnd(name, pair.ending) + " (" + pair.replacement + ")")
                .orElse(name);
    }

    private record Replacement(String ending, String replacement) {
    }
}
