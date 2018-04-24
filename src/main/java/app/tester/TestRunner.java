package app.tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    List<Method> setups = new ArrayList<>();
    List<Method> tests = new ArrayList<>();
    List<Method> teardowns = new ArrayList<>();

    for (Method method : testSuite.getClass().getDeclaredMethods()) {
      if (method.getAnnotation(Setup.class) != null)
        setups.add(method);
      if (method.getAnnotation(TestMethod.class) != null)
        tests.add(method);
      if (method.getAnnotation(Teardown.class) != null)
        teardowns.add(method);
    }

    List<TestResult> results = new ArrayList<>();
    for (Method test : tests) {
      for (Method setup : setups)
        setup.invoke(testSuite);
      long start = System.currentTimeMillis();
      boolean passed = runTest(testSuite, test);
      long end = System.currentTimeMillis();
      for (Method teardown : teardowns)
        teardown.invoke(testSuite);
      results.add(new TestResult(test.getName(), passed, end - start));
    }
    return results;
  }

  private boolean runTest(Object testSuite, Method test) throws IllegalAccessException {
    ExpectedException expected = test.getAnnotation(ExpectedException.class);
    try {
      test.invoke(testSuite);
      if (expected != null)
        return false; // expected some exception, but test didn't throw
    } catch (InvocationTargetException ite) {
      if (expected == null)
        return false; // didn't expect any exception
      if (!expected.value().equals(ite.getTargetException().getClass()))
        return false; // didn't expect that exception
    }
    return true;
  }
}
