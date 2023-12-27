## Interview Questions

Topics: Java, Spring, Relational Databases, Search Functionality

### How do you sort a collection of objects by ont of its property?

Pass a new Comparator which compares the two properties as a paramter into Collections.sort.

```
Collections.sort(employeeList, new Comparator<Employee>( {
    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.getSalary() - e1.getSalary();
    }
}));
```

Or, have the class implement the interface Comparable, then define the method `compareTo`.

```
public class Employee implements Comparable<Employee> {
    private int salary;

    public int compareTo(Employee e) {
        return getSalary() - e.getSalary();
    }
}

Collections.sort(employeeList);
```

### How do you check if two objects are equal?

Even if the properties of `Object1` and `Object2` are equal, simply checking `Object1 == Object2` may return false, since this will only check if they are the same reference. To create a function that will check if the properties of two objects of a type are equal, you can create a new method `equals` in that class to compare specific properties. This method will also need the `@Override` annotation in order to override the `equals` method in the base `Object` class. The `equals` method accepts parameter of type `Object`, this is so it overrides and not overloads the method. Also, if `equals` is being overriden, then `hashCode` must also be in case of using the object in a collection that uses hashes, such as HashSet.

```
class Employee {
    private String name;
    private int salary;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (!(o instanceof getClass()))
            return false;
        final Employee other = (Employee) o;
        return getName().equals(other.getName()) && getSalary() == other.getSalary();
    }

    @Override
    public int hashCode() {
        Objects.hash(getName(), getSalary());
    }
}
```

### What are examples of Design Patterns in Java?

Creational:
* Singleton: A class that there is only one instance of. Use the keyword "final" in the class definition to tell the compiler there can only be one instance of it.
```
// volatile and synchronized provide thread-safeness
public *final* class Singleton {
    private static volatile Singleton instance;
    public static Singleton getInstance() { 
        Singleton s = instance;
        if (s != null) return s;
        synchronized(Singleton.class) {
            if (instance == null)
                instance = new Singleton();
            return instance;
        }
    }
}
```

* Factory: A specialized method to create new instances. Overriding the method in subclasses can create subclasses of instances.
```
class A {
    protected Asset createAsset() {
        return new MyAsset();
    }

    public void ex() {
        Asset b = createAsset();
    }
}
```

* Abstract Factory: An interface intended for creating instances. It may be implemented by specialized factories to create specific types of instances.
```
interface AbstractFactory {
    Asset createAsset();
}

class A {
    private AbstractFactory fac;

    public A (AbstractFactory fac) {
        this.fac = fac;
    }

    public void ex() {
        Asset b = factory.createAsset();
    }
}
```

* Builder: An interface to be implemented with code to construct parts of another object.
```
Employee.builder() // <- EmployeeBuilder
    .name("Bob")
    .salary(1)
    .build();
```


* Prototype: Applying a common interface for multiple objects that can accept and pass on specialized info from an implementation.

Structural:
* Bridge: An abstraction of a dimension of a class to support multiple implementations.
```
class Employee {
    private Salary mySalary;

    public int getSalary() {
        return mySalary.getValue();
    }
}

interface Salary {}

employee.getSalary();
```

* Composite: Share an interface between different levels of a tree.
```
public interface Node {
    public int getTotalValue();
}

public class Parent implements Node {
    private Node[] children;
    public int getTotalValue() {
        children.stream().mapToInt(Node::getTotalValue).sum();
    }
}
```

* Adapter: A class which stores a reference to an object that would other not be compatible with another interface.
```
class NonAssetAdapter implements Asset {
    private NonAsset B;

    // asset methods
}
```

* Decorator: A wrapper object to add functionality, instead of extending it.
```
class Employee { ... }

class Worker {
    private Employee e;
    public void doWork() { ... }
}
```

* Facade: A wrapper object which hides the collaboration of multiple classes to expose a simple interface.
```
class MyFacade {
    private Class1 c1;
    private Class2 c2;
    private Class3 c3;

    public void execute() {
        c1.use(c2).with(c3);
    }
}
```

* Proxy: A wrapper object to provide access to another system. Adapter, Decorator, Facade, and Proxy are mostly similar in implementation and only differ in intent.
* Flyweight: Moving state constant across multiple objects into its own class.

Behavorial:
* Command: An object representing a request to be passed as an argument.
* Iterator: An object that returns pieces of an underlying structure in order.
* Observer: Store a list of subscribers to which some state is published.
* Strategy: Store different algorithms in specialized objects that share an interface.
* Chain of Responsibility: Performing actions sequentially in a list of handlers with no branches, each of which decides to pass to the next or process the request.
* Mediator: Having a parent for individual components that need to communicate with each other rather than having them communicate directly.
* Template Method: Have subclasses modify a superclass's algorithm.
* Memento: Have an method to copy the object's state into a new object.
* Visitor: A object containing specialized code to be accepted as an argument by multiple objects.
```
class Employee {
    interview(Interviewer i) {
        i.interview();
    }
}
```


### In Spring, how to ascertain that a Bean being referenced is a specific instance?

The annotation `@Autowired` is often used to inject dependencies. If there are multiple Beans of the same type, however, one can be selected using another annotation `@Qualifier`.

```
@Component
@Qualifier("myformatter")
public class MyFormatter implements Formatter {}

...

public class MyService {
    @Autowired
    @Qualifier("myformatter")
    private Formatter formatter;
}
```

### What are PRIMARY, FOREIGN, and UNIQUE keys?
Primary keys are the sole identifier for rows in a table. Primary keys must be unique. Unique keys must also be unique, but they can also accept NULL for a value, albeit for only one.

A foreign key references a key of another row, usually the primary key in another table.

### What is a tokenizer? What are stop words?
A tokenizer takes text and divides it into tokens, usually words or phrases. A stop word is a commonly used token that doesn't add any value ("am", "an", "the") and are removed.