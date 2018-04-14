package app.tester;

import java.util.List;

public class TestRunner {

  /**
   * Runs all methods in the given object (test suite) that are annotated
   * with {@link TestMethod} and match the prerequisites described there.
   * <p>
   * Each methods annotated with {@link TestMethod} represents a test case
   * that can either fail or succeed.
   * <p>
   * The test suite can contain additional methods that must be run before
   * or after each test method. These are annotated with {@link Setup} and
   * {@link Teardown}.
   *
   * @return a list of {@link TestResult} that contains a result for each test method.
   */
  public List<TestResult> runTests(Object testSuite) throws Exception {
    // use reflection to find methods and annotations from testSuite
    // run the methods using reflection
    return null; // TODO implement
  }
}
