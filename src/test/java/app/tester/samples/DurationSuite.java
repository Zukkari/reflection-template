package app.tester.samples;

import app.tester.ExpectedException;
import app.tester.TestMethod;

public class DurationSuite {

  public final int durationPassing = 50;
  public final int durationPassingExceptionally = 75;
  public final int durationFailing = 100;

  @TestMethod
  public void testPassing() throws InterruptedException {
    Thread.sleep(durationPassing);
  }

  @TestMethod
  @ExpectedException(IllegalArgumentException.class)
  public void testPassingWithException() throws InterruptedException {
    Thread.sleep(durationPassingExceptionally);
    throw new IllegalArgumentException();
  }

  @TestMethod
  public void testFailing() throws InterruptedException {
    Thread.sleep(durationFailing);
    throw new IllegalArgumentException();
  }
}
