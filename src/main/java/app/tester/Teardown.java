package app.tester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a method that must be executed after each test is finished.
 * <p>
 * The teardown methods are searched from the provided class, including all its superclasses.
 * If a Teardown method fails, then the entire test suite is aborted with an exception.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Teardown {
}
