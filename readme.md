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

# Alternatives to reflection

As many of you might have noticed, handling all the exceptions when operating with reflection might be cumbersome.
Moreover, Oracle plans to get rid of reflection eventually in favor of other tools.
With changes from Java 9 (introduction of modules), reflective operations might output a warning which most of you have 
already seen:

```text
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by io.netty.util.internal.ReflectionUtil (file:/home/azureuser/server-0.28.0-SNAPSHOT.jar) to constructor java.nio.DirectByteBuffer(long,int)
WARNING: Please consider reporting this to the maintainers of io.netty.util.internal.ReflectionUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

In short, this means that reflective access will be forbidden in the future, so authors of the libraries that
use reflection should rework their libraries to get rid of reflection.

But as we have seen, reflection provides some ways to change objects behaviour at runtime of the application, 
so there should be an alternative for that, right?

This is the part where method handles and variable handles come in.

## Method handles

In Java 7, class `MethodHandles` was introduced to the Java API.

From the documentation, the purpose of this class is to:

- Lookup methods which help create method handles for methods and fields.
- Combinator methods, which combine or transform pre-existing method handles into new ones.
- Other factory methods to create method handles that emulate other common JVM operations or control flow patterns.

In short, this class provides alternative ways to manipulate methods at runtime.
Here is an example how to call a method at the runtime, without the use of the `java.lang.reflect` package:

```java
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

class MethodHandleTest {
    public static void main(String[] args) throws Throwable {
        var testClass = new TestClass();

        // Create a lookup object were we will look for the method to invoke
        final var lookup = MethodHandles.lookup();

        // Create a handle that we will use to invoke the method on a object
        final var handle = lookup.findVirtual(
                TestClass.class, // Class to search for the method
                "sayHello", // Name of the method to look up
                MethodType.methodType(void.class) // Return type of the method
        );

        // Invoke the handle on a object to execute the method on the target object
        handle.invokeExact(testClass);
    }
}

class TestClass {
    public void sayHello() {
        sayHello("world");
    }

    private void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
```

Yeah, this is cool and all, but what about private methods with parameters? 
Well, method handles can also do that:

```java
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

class MethodHandleTest {
    public static void main(String[] args) throws Throwable {
        var testClass = new TestClass();

        // Create a lookup object that can lookup private members in a specific class
        final var lookup = MethodHandles.privateLookupIn(TestClass.class, MethodHandles.lookup());

        // Create a handle that we will use to invoke the method on a object
        final var handle = lookup.findVirtual(
                TestClass.class, // Class to search for the method
                "sayHello", // Name of the method to look up
                MethodType.methodType(void.class, String.class) // Return type of the method (first parameters is return type, then types of the method arguments)
        );

        // Invoke the handle on a object to execute the method on the target object
        handle.invokeExact(testClass, "MethodHandles");
    }
}

class TestClass {
    public void sayHello() {
        sayHello("world");
    }

    private void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
```

The only difference here is that we have to create another lookup object, which is able to
lookup private members in a specific class.
Other steps are pretty straightforward and intuitive.

So method handles are a neat feature which allows you to handle methods at runtime.
Why is it better than reflection?
Well, there are several advantages that include but are not limited to:

- Type safety
- Cleaner API (fewer exceptions to handle)
- Method handles can be cached in order to improve performance

So method handles allow you to do amazing things during the runtime.
Here is an [article](https://dzone.com/articles/hacking-lambda-expressions-in-java) which covers this topic in depth
(but beware that this article can be fully only understood by the mightiest Java wizards).

## Var handles

Previously we investigated method handles.
But what about variables?
Reflection also allows us to access private variables and set their values.

Well, in Java 12 variable handles were introduced, which allow us to manipulate fields of the objects.
Since we are using Java 11, then there is no way to modify private fields without using reflection.

However, lets look at an example how we can modify a private field of a class using variable handles:

```java
import java.lang.invoke.MethodHandles;

class MethodHandleTest {
    public static void main(String[] args) throws Throwable {
        var testClass = new TestClass();
        testClass.printState();
        
        final var varHandle = MethodHandles.privateLookupIn(TestClass.class, MethodHandles.lookup())
                .findVarHandle(
                        TestClass.class, // Class where to find variable in
                        "internalState", // Name of the variable to look for
                        String.class // Type of the variable
                );

        // Update the value of the variable
        varHandle.set(testClass, "newInternalState");

        testClass.printState();
    }
}

class TestClass {
    private String internalState = "internalState";

    public void printState() {
        System.out.println(internalState);
    }
}
```

As you might have noticed, the API is quite similar to the method handles API.
There is, however, a "major" disadvantage of using var handles over reflection.
Using variable handles, there is no way update a final field of a class.
But in most cases, this should be considered as an advantage, because immutable state should stay immutable.

In practice tasks, it is up to you to decide which approach you would like to use when solving the tasks.

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
