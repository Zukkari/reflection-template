# Java Reflection API

Reflection allows the program to inspect objects and classes at **runtime**.
Given any object, the reflection API allows the following:

 * get the class of the object
 * list all methods present in the class
 * list all fields present in the class
 * list all annotations present on a class/method/field
 * create an object using any constructor
 * call any method of a class/object
 * get/set any field of a class/object

Reflection allows some dirty-dirty hacks, including setting private
and even final fields (please don't do that), calling private methods
from outside that class, calling private constructors etc.

Reflection is mainly used by magical libraries to make life easier for the developers.
Magic always has a tradeoff - more magic means it's less obvious how the library works.
Less obvious code is more difficult to understand and use right. If you manage to
learn the magic, then it can help you avoid some work.

Access to the reflection API usually starts with a Class object.
There are three main options for getting the Class object:

 * `Class<?> c = MyKlass.class;`
 * `Class<?> c = myObject.getClass();`
 * `Class<?> c = Class.forName("my.package.MyClass");`

## Useful tools in the reflection API

### java.lang.Class

 * `String getName()`
 * `Class<?> getSuperclass()`
 * `Field[] getDeclaredFields()`
 * `Method[] getDeclaredMethods()`
 * `T getAnnotation(Class<T> annotationType)`

### java.lang.reflect.Field

 * `String getName()`
 * `Object get(Object obj)`
 * `void set(Object obj, Object value)`
 * `void setAccessible(boolean flag)`
 * `T getAnnotation(Class<T> annotationType)`

### java.lang.reflect.Method

 * `String getName()`
 * `Object invoke(Object obj, Object... args)`
 * `void setAccessible(boolean flag)`
 * `T getAnnotation(Class<T> annotationType)`

## Examples

### Listing all methods in a class
```
// list all methods in class Test
for (Method method : Test.class.getDeclaredMethods()) {
  System.out.println(method.getName());
}
```

### Setting a public field of an object
```
// create a Test object
// set the value of its field someString to 'newValue'
Test test = new Test();
Test.class.getDeclaredField("someString").set(test, "newValue");
```

### Invoking a method
```
class Sample {
  public String hello() {
    return "Hello world!";
  }
}

Sample s = new Sample();
Method hello = Sample.class.getDeclaredMethod("hello");
Object result = hello.invoke(s);
System.out.println(result); // Hello world!
```

## Accessing public and private members

Methods getConstructor, getMethod and getField in class Class will only search for
members that are public. The members are also searched from any superclasses.

Methods getDeclaredConstructor, getDeclaredMethod and getDeclaredField will search
for members that have any access modifiers, including private members. The declared
variants **do not** search the member from the class's superclasses. If you want
to find private members of a superclass, then do `getSuperclass()` and search the
returned value.

By default, accessing private members is not permitted. Before using a private member
you must first call `setAccessible(true)` on it. This works 99% of the time, but
the JVM can be configured to disallow accessing private members for security reasons.

## Annotations

Annotations are metadata that can be attached to classes, fields, methods, method parameters, etc.
Annotations on their own don't do anything - they just exist and hold some data.
However, a program can inspect classes at runtime to find the annotations, read the data and made decisions based on it.

### Creating and using annotations

An annotation declaration is similar to that of an interface.

```
@Retention(RetentionPolicy.RUNTIME)
@interface Range {
  double min();
  double max();
}
```

The annotation can then be added to classes/fields/methods etc.

```
class Ticket {

  @Range(min = 0.99, max = 15.99)
  public double price;
}
```

A running program can inspect any class for annotations and make decisions based on them.

```
class Validator {

  void validate(Object obj) throws Exception {
    for (Field field : obj.getClass().getDeclaredFields()) {
      Range rangeValidator = field.getAnnotation(Range.class);
      if (rangeValidator != null) {
        double fieldValue = field.getDouble(obj);
        if (fieldValue < rangeValidator.min())
          throw new RuntimeException(field.getName() + " is too small");
        if (fieldValue > rangeValidator.max())
          throw new RuntimeException(field.getName() + " is too large");
      }
    }
  }
}
```

# When to use reflection

In most cases reflection is not needed.
Using interfaces is more readable, better maintainable and has better performance.
Use reflection when using interfaces is not possible or not practical.
Usually reflection is used when writing libraries that other people from other projects will later use.
The following tasks are good example use cases for using reflection.

# Practice tasks

## QueryGenerator

Add the missing functionality in `app.db.QueryGenerator` so that the tests in `QueryGeneratorTest` pass.

The `QueryGenerator` class works similar to how the Hibernate ORM library works.
The `QueryGenerator` should help with inserting Java objects into SQL databases.
Its `generateInsertStatement` method takes an arbitary object as a parameter and generates a suitable SQL insert statement for it.

Example:
```
class Customer {

  private String name;
  private String phoneNumber;

  Customer(String name, String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
}

public class Example {
  public static void main(String[] args) throws Exception {
    Customer bob = new Customer("Bob", "+372 123 4567");
    Query query = new QueryGenerator().generateInsertStatement(bob);
    System.out.println(query.getQuery());
    // INSERT INTO Customer (name, phoneNumber) VALUES (?, ?);
    System.out.println(query.getParameters());
    // ["Bob", "+372 123 4567"]
  }
}
```

The `QueryGenerator` doesn't need to insert anything into an actual database.
It just needs to generate the query string and find the parameter values.

The `QueryGenerator` uses the object's class name as the table name and the object's field names as the database column names.
The placeholder value `?` is added for each column.
Finally, it also gets the object's field values and stores them as query parameter values.

## TestRunner

Add the missing functionality in `app.tester.TestRunner` so that the tests in `TestRunnerTest` pass.

The `TestRunner` class should work similar to how JUnit tests work.
An object containing some test methods (a test suite) is passed to the `TestRunner`.
It will inspect the object, find all the methods marked with the `@TestMethod` annotation and run them.
When a test method throws an exception, then the test is marked as failed.
Otherwise the test is marked as passed.

Example:

```
class MyTestSuite {

  @TestMethod
  public void testOnePlusOneIsTwo() {
    int sum = 1 + 1;
    if (sum != 2)
      throw new RuntimeException("test failed!");
  }

  @TestMethod
  public void successIsEightLetters() {
    int length = "success".length();
    if (length != 8)
      throw new RuntimeException("test failed!");
  }
}

public class Example {
  public static void main(String[] args) throws Exception {
    MyTestSuite suite = new MyTestSuite();
    List<TestResult> results = new TestRunner().runTests(suite);
    for (TestResult result : results) {
      System.out.println(result.getTestName() + result.isPassed());
    }
    // testOnePlusOneIsTwo true
    // successIsEightLetters false
  }
}
```
