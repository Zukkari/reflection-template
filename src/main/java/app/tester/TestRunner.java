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
    // TODO run the tests in the suite and store the results
    // 1) find all the methods marked with @TestMethod
    // 2) run the methods one by one
    // 3) for each test, store if it passed and the time spent to run the test
    //    hint: use System.currentTimeMillis() and try-catch-finally
    // run the tests to see what's missing
    return null;
  }
}
