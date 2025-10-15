# oop-jsf-binding

## Überblick

Dieses Projekt ist eine Beispielanwendung zur Demonstration von objektorientierten Prinzipien (OOP) in einer Java-Webanwendung mit Jakarta EE und PrimeFaces. 
- Die Anwendung verwaltet Aufgaben (`Task`) und Ordner (`Folder`) in einer hierarchischen Struktur und zeigt, wie UI-Komponenten direkt an die Domänenobjekte gebunden werden können.

## Schnell Start

run MAVEN commands in the root folder of the Java project:

- for server deployment as .war <br/>
``mvn initialize``

- for standalone app as .jar <br/>
``mvn pakage``

Die Anwendung kann als WAR in einem Jakarta EE-kompatiblen Server (z.B. WildFly, Payara) oder lokal mit einer H2-In-Memory-Datenbank gestartet werden.

## Architektur und OOP-Prinzipien

Die Anwendung folgt den klassischen OOP-Prinzipien:

- **Abstraktion:** Die Interfaces [`org.task.Task`](src/main/java/org/task/Task.java) und [`org.task.Folder`](src/main/java/org/task/Folder.java) definieren die grundlegenden Operationen für Aufgaben und Ordner, unabhängig von deren konkreter Implementierung.
- **Kapselung:** Die Implementierungen [`org.task.db.DbTask`](src/main/java/org/task/db/DbTask.java) und [`org.task.db.DbFolder`](src/main/java/org/task/db/DbFolder.java) kapseln die Datenbanklogik und stellen Methoden zur Anzeige und Persistenz bereit.
- **Vererbung:** Die Anwendung nutzt Vererbung, z.B. durch die Erweiterung von Framework-Klassen wie `LazyDataModel<Task>` in [`org.task.ui.TaskTable`](src/main/java/org/task/ui/TaskTable.java).
- **Polymorphie:** Die UI-Komponenten und Controller arbeiten mit den Interfaces, sodass verschiedene Implementierungen (z.B. für Tests oder andere Datenquellen) einfach ausgetauscht werden können.

## Komponenten

- **Domain Layer:** Die Interfaces und deren Implementierungen für Aufgaben und Ordner.
- **Persistence Layer:** Die Klasse [`org.task.db.H2Db`](src/main/java/org/task/db/H2Db.java) initialisiert die Datenbank und stellt Testdaten bereit.
- **Presentation Layer:** Die Managed Beans wie [`org.task.ui.TaskForm`](src/main/java/org/task/ui/TaskForm.java) binden die UI-Komponenten direkt an die Domänenobjekte.
- **Web Layer:** REST-API-Endpunkte wie [`org.task.web.TaskResource`](src/main/java/org/task/web/TaskResource.java) ermöglichen den Zugriff auf Aufgaben als JSON-Ressourcen.

## UI-Binding

Die Anwendung zeigt, wie JSF-Komponenten über das `binding`-Attribut direkt mit den Methoden der Domänenobjekte verbunden werden können. 
- **Beispiel:** Die Methode `displayTo(HtmlOutputText output)` im Task-Objekt setzt die Darstellung direkt auf das UI-Element.



## Beispielhafte OOP-Implementierung

- Aufgaben und Ordner werden als Objekte modelliert, die ihre eigene Persistenz und Anzeige steuern.
- Die UI-Komponenten werden direkt von den Objekten aktualisiert, z.B. durch Methoden wie `displayTo(Tree tree)` oder `displayTasksTo(DataTable table)`.

## Weiterführende Links

- [src/main/java/org/task/Task.java](src/main/java/org/task/Task.java)
- [src/main/java/org/task/Folder.java](src/main/java/org/task/Folder.java)
- [src/main/java/org/task/db/DbTask.java](src/main/java/org/task/db/DbTask.java)
- [src/main/java/org/task/db/DbFolder.java](src/main/java/org/task/db/DbFolder.java)
- [src/main/java/org/task/ui/TaskForm.java](src/main/java/org/task/ui/TaskForm.java)
- [src/main/java/org/task/web/TaskResource.java](src/main/java/org/task/web/TaskResource.java)
