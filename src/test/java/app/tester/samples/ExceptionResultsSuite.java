package app.tester.samples;

import app.tester.ExpectedException;
import app.tester.TestMethod;

public class ExceptionResultsSuite {

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testThrowExpectedIAE() {
    throw new IllegalArgumentException();
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testExpectedIAEButThrowsNPE() {
    throw new NullPointerException();
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testExpectedIAEButNoThrow() {
  }
}
