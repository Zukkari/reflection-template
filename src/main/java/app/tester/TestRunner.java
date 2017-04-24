package app.tester;

import java.util.List;

public class TestRunner {

  /**
   * Runs all valid {@link TestMethod} methods in the given test suite.
   *
   * @return a list of {@link TestResult} that contains a result for each test method.
   */
  public List<TestResult> runTests(Object testSuite) throws ReflectiveOperationException {
    // use reflection to find methods and annotations from testSuite
    // run the methods using reflection
    return null; // TODO implement
  }
}
