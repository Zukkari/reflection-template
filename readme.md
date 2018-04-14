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
 * `Constructor<?>[] getDeclaredConstructors()`
 * `Field[] getDeclaredFields()`
 * `Method[] getDeclaredMethods()`
 * `Annotation[] getDeclaredAnnotations()`

### java.lang.reflect.Constructor\<T\>

 * `Class<?>[] getParameterTypes()`
 * `Annotation[] getDeclaredAnnotations()`
 * `T newInstance(Object... initargs)`
 * `void setAccessible(boolean flag)`

### java.lang.reflect.Field

 * `String getName()`
 * `Class<?> getType()`
 * `Annotation[] getDeclaredAnnotations()`
 * `Object get(Object obj)`
 * `void set(Object obj, Object value)`
 * `void setAccessible(boolean flag)`

### java.lang.reflect.Method

 * `String getName()`
 * `Class<?>[] getParameterTypes()`
 * `Class<?> getReturnType()`
 * `Annotation[] getDeclaredAnnotations()`
 * `Object invoke(Object obj, Object... args)`
 * `void setAccessible(boolean flag)`

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

### Reading an annotation
```
@MyAnnotation("classValue")
class Sample {

  @MyAnnotation("fieldValue")
  Object myField;
}

MyAnnotation classAnnotation = Sample.class.getAnnotation(MyAnnotation.class);
System.out.println(classAnnotation.value()); // classValue

Field myField = Sample.class.getDeclaredField("myField");
MyAnnotation fieldAnnotation = myField.getAnnotation(MyAnnotation.class);
System.out.println(fieldAnnotation.value()); // fieldValue
```

# Practice tasks

Implement the missing functionality.

 * change app.db.QueryGenerator so that tests in QueryGeneratorTest pass
 * change app.tester.TestRunner so that tests in TestRunnerTest pass
