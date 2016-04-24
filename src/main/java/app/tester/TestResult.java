package app.tester;

public class TestResult {

  private final String testName;
  private final boolean passed;
  private final long durationMillis;

  public TestResult(String testName, boolean passed, long durationMillis) {
    this.testName = testName;
    this.passed = passed;
    this.durationMillis = durationMillis;
  }

  /**
   * Name of the tested method
   */
  public String getTestName() {
    return testName;
  }

  /**
   * Result of the test as described in {@link TestMethod}
   */
  public boolean isPassed() {
    return passed;
  }

  /**
   * Time in milliseconds from the start of the test method execution
   * until the method returned or exited with an exception.
   */
  public long getDurationMillis() {
    return durationMillis;
  }

  @Override
  public String toString() {
    return String.format(
        "TestResult{testName='%s', passed=%s, duration=%d}",
        testName, passed, durationMillis);
  }
}
