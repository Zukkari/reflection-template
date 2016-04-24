package app.tester.samples;

import app.tester.Teardown;
import app.tester.TestMethod;

public class TeardownSuite extends TeardownSuper {

  @Teardown
  public void after2() {
    after2++;
    if (after2 != testCycle)
      failure = new IllegalStateException("@Teardown must run after the test");
  }

  @TestMethod
  public void testFirst() {
    testCycle++;
    if (after1 != testCycle - 1 || after2 != testCycle - 1)
      failure = new IllegalStateException("@Teardown is expected to run after the test");
  }

  @TestMethod
  public void testSecond() {
    testCycle++;
    if (after1 != testCycle - 1 || after2 != testCycle - 1)
      failure = new IllegalStateException("@Teardown is expected to run after the test");
    throw new NullPointerException("intentional");
  }
}
