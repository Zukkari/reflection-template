package app.tester.samples;

import app.tester.Teardown;

public class TeardownSuper {

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
}
