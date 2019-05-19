---
title: Fulfilled the Four Principles of Object-oriented Programming
img: assets/models-package-uml.png
---

##### Encapsulation & Abstraction

`TaskBase`'s fields are encapsulated and only accessed using getters/setters.
The `children` field is also fully abstracted and has a generic datatype.

```java
private String id;
private String name;
private String description;
private ObservableList<? extends TaskBase> children;

TaskBase(..., ObservableList<? extends TaskBase> children) {
    ...
    this.children = children;
}
```

##### Inheritance & Polymorphism

Extending the `TaskBase` allows treating Board, Column & Task Uniformly (_Composite design pattern_)

```java
abstract class TaskBase { ... }
class Board extends TaskBase { ... }
class Column extends TaskBase { ... }
class Task extends TaskBase { ... }
```