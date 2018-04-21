package app.tester.samples;

import app.tester.Teardown;
import app.tester.TestMethod;

public class TeardownSuite {

  public int testCycle = 0;
  public int after1 = 0;
  public int after2 = 0;
  public Exception failure;

  @Teardown
  public void after1() {
    after1++;
    if (after1 != testCycle)
      failure = new IllegalStateException("@Teardown must run after the test");
  }

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
    throw new RuntimeException("this test should be reported as failed");
  }
}
