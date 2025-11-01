# OOP-Jsf-Binding

## Overview

Demonstrating **Object-Oriented Programming (OOP) principles** where objects controls their own persistence and display logic within a Java web application using Jakarta EE and PrimeFaces.
* The application manages tasks (Task) and folders (Folder) in a hierarchical structure and showcases how **UI components can be directly bound to domain objects**.
* The application can be started as a *WAR* on a Jakarta EE compliant server (e.g., WildFly, Payara) or locally as a *JAR* (with embedded WildFly), using an H2 in-memory database.

## Quick Start
**Required:** Java 11+
1. run in cmd: ``mvn package`` - standalone application - as executable **.jar**
* or optional: ``mvn install`` - jee server application - as deployable  **.war** 
2. then browse: [http://localhost:8080/index.xhtml](http://localhost:8080/index.xhtml)

## **Architecture and OOP Principles**

The application adheres to classic OOP principles:

* **Abstraction:** The interfaces [org.todo.Task](https://github.com/andreas-wagner-dev/oop-jsf-binding/tree/main/src/main/java/org/todo/Task.java) and [org.todo.Folder](https://github.com/andreas-wagner-dev/oop-jsf-binding/tree/main/src/main/java/org/todo.Folder.java) define the fundamental behaviar as operations for tasks and folders, independent of their concrete implementation.  
* **Encapsulation:** The implementations [org.todo.task.DbTask](https://github.com/andreas-wagner-dev/oop-jsf-binding/tree/main/src/main/java/org/todo)/DbTask.java  and [org.task.db.DbFolder](https://www.google.com/search?q=src/main/java/org/task/db/DbFolder.java) encapsulate database logic and provide methods for display and persistence.  
* **Inheritance:** The application uses inheritance, for instance, by extending framework classes like LazyDataModel\<Task\> in [org.task.ui.TaskTable](https://www.google.com/search?q=src/main/java/org/task/ui/TaskTable.java).  
* **Polymorphism:** UI components and controllers work with the interfaces, allowing various implementations (e.g., for testing or different data sources) to be easily swapped.

## **Components**

* **Application:** The root Composition of all implementations: Strartup, DI and Properties.  
* **Domain:** The interfaces and their implementations for tasks and folders.  
* **Persistence:** The class [org.task.db.H2Db](https://www.google.com/search?q=src/main/java/org/task/db/H2Db.java) initializes the database and provides test data.  
* **Presentation:** Managed Beans like [org.task.ui.TaskForm](https://www.google.com/search?q=src/main/java/org/task/ui/TaskForm.java) bind UI components directly to the domain objects.  
* **Webservice:** REST API endpoints such as [org.task.web.TaskResource](https://www.google.com/search?q=src/main/java/org/task/web/TaskResource.java) allow access to tasks as JSON resources.

## **UI Binding**

The application demonstrates how JSF components can be connected directly to the domain object methods via the **binding** attribute.

* **Example:** The `displayTo(HtmlOutputText output)` method in the Task object directly sets the representation onto the UI element.

## **Exemplary OOP Implementation**

* Tasks and folders are modeled as objects that control their own persistence and display logic.  
* The UI components are updated directly by the objects themselves.  
* E.g., through methods like `displayTo(Tree tree)` or `displayTasksTo(DataTable table)`.


## **Project Links**
* [src/main/java/org/task/Task.java](https://www.google.com/search?q=src/main/java/org/task/Task.java)  
* [src/main/java/org/task/Folder.java](https://www.google.com/search?q=src/main/java/org/task/Folder.java)  
* [src/main/java/org/task/db/DbTask.java](https://www.google.com/search?q=src/main/java/org/task/db/DbTask.java)  
* [src/main/java/org/task/db/DbFolder.java](https://www.google.com/search?q=src/main/java/org/task/db/DbFolder.java)  
* [src/main/java/org/task/ui/TaskForm.java](https://www.google.com/search?q=src/main/java/org/task/ui/TaskForm.java)  
* [src/main/java/org/task/web/TaskResource.java](https://www.google.com/search?q=src/main/java/org/task/web/TaskResource.java)

## **References**

[Writing Properties Bound to Component Instances.](https://javaee.github.io/tutorial/jsf-develop002.html)


