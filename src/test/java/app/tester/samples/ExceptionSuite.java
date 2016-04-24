package app.tester.samples;

import app.tester.ExpectedException;
import app.tester.TestMethod;

public class ExceptionSuite {

  public static final long SLEEP_NOTHROW = 65;
  public static final long SLEEP_THROW_EXPECTED = 50;
  public static final long SLEEP_THROW_UNEXPECTED = 35;

  @TestMethod
  public void testNoThrow() throws InterruptedException {
    Thread.sleep(SLEEP_NOTHROW);
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testThrowExpectedIAE() throws InterruptedException {
    Thread.sleep(SLEEP_THROW_EXPECTED);
    throw new IllegalArgumentException("intentional");
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testExpectedButThrowsDifferent() throws InterruptedException {
    Thread.sleep(SLEEP_THROW_UNEXPECTED);
    throw new NullPointerException("intentional");
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testExpectedButNoThrow() throws InterruptedException {
    Thread.sleep(SLEEP_NOTHROW);
  }

  @TestMethod
  public void testThrowUnexpectedIAE() throws InterruptedException {
    Thread.sleep(SLEEP_THROW_UNEXPECTED);
    throw new IllegalArgumentException("intentional");
  }
}
