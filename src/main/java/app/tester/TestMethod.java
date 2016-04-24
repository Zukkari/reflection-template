package app.tester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a test method that must be executed by the test runner.
 * All {@link Setup} methods must run before the test method.
 * All {@link Teardown} methods must run after the test method.
 * The test method is marked failed if it throws an exception and the test doesn't have the {@link ExpectedException}
 * annotation or the exception type doesn't match the type specified by {@link ExpectedException}.
 *
 * The test method must be a non-static no-args public method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestMethod {
}
