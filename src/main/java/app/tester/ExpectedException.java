package app.tester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the test method must only be considered successful if it throws the specified exception.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectedException {

  /**
   * Type of the exception the method is expected to throw
   */
  Class<? extends Throwable> value();
}
