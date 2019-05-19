---
title: Custom Exceptions
img: assets/exceptions-package-uml.png
---

The `exceptions` package contains a Parent customized Exception `OCTException` and other sub-exceptions, that allows catching any instance of the sub-exceptions using `OCTException`.

```java
try {

    try {
        ....
    } catch (FileNotFoundException e) {
        throw new FirebaseKeyFileNotFoundException();
    }

    try {
        ...
    } catch (IOException e) {
        throw new InvalidFirebaseKeyException();
    }

} catch (OCTException e) {
    // use e.printError();
    // or e.displayErrorView(rootStackPane);
}

```
