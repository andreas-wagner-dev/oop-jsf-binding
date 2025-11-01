# JSF-Template-Application

* [About](#about)
* [Showcase](#showcase)
* [Tools](#tools)
* [Technology](#technology)
* [Components](#components)
* [Environment](#environment)
* [Deployment](#deployment)
* [TODO](#todo)

## About
**Summary**<br/><br/>
This is a template of an Java Server Faces (JSF) application, besed on Java EE Technologies.<br/><br/>

**Quick Start** <br/><br/>

TODD: tbd 

## Showcase

TODD: tbd
## Principles



## Packages
**Packages and Namespaces of this projects**<br/><br/>
https://javadevguy.wordpress.com/2017/12/18/happy-packaging/
https://javadevguy.wordpress.com/2019/06/06/data-boundaries-are-the-root-cause-of-maintenance-problems/
<br/>
- Packages should never depend on sub-packages.
- Sub-packages should not introduce new concepts, just more details.
- Packages should reflect business-concepts, not technical ones.

**Root-Package**<br/>
It would be convenient if the key abstractions which do not depend on anything else would be in the top (root) package, or else it would require a dependency analyzer to find the “entry point” into the application. So if somebody asks the question: What is this application/library about? The answer should be in the root package.<br/>

**Sub-Packages**<br/>
To have a clear hierarchy, in which the root package does not depend on any of the sub-packages. This means a reader can start with the classes there, without having to understand what the sub-packages are about. Some of the top level classes are:<br/>

All classes listed  in the root package and most of the other ones on this level are pure interfaces with proper business methods, so the overall logic and feature set of the library can be understood just by looking at the root package. The sub-packages do not introduce new functionality, just more details, specializations, implementations of the root classes.
<br/><br/>
- 1 --- app.name<br/>
- 2 ------- business-concepts-1<br/>
- 2 ------- business-concepts-2<br/>
<br/><br/>
- com.company
    - Algorithm interface
    - algorithm package
        - AlgorithmImpl class
        - AlgorithmStep interface
        - algorithmstep package
            - AlgorithmStepImpl1
            - AlgorithmStepImpl2

Basically invert the relationship. 
<br/><br/>
- Instead of instantiating the lower-level stuff from higher-level,<br/>
- instantiate the higher-level thing from the lower-level class with the lower-level object already configured.

**Stable Abstractions Principle**
Robert C. Martin proposed the idea that for well designed software there should be a specific
relationship between two package measures: the abstractness of a package, which shall express
the portion of contained abstract types, and its stability, which indicates whether the package is
mainly used by other artifacts (stable) or if it mainly depends on other artifacts (instable). The
desired relationship is captured in the Stable Abstractions Principle: A package should be as
abstract as it is stable. By sticking to this principle we avoid getting packages which are used
heavily by the rest of the application and which, at the same time, have a low degree of
abstraction. Such packages are a constant source of trouble, since they are hard to change or
extend.

## Tools

<h3>Java Version</h3> (Java Development Kit)

- **Java (JDK) 17+** download: <https://www.oracle.com/de/java/technologies/downloads>

<h3>IDE Tool</h3> (Integrated Development Environment)

- **Eclipse** download: <https://www.eclipse.org/downloads/packages/> IDE for Enterprise Java and Web Developers

<h3>Build Tool</h3> (Dependencies Management and Build Tool)

- **Maven** download: <https://maven.apache.org>

<h3>Repository</h3>

[See the definiton of terminologies] <https://www.tutorialspoint.com/gitlab/index.htm> across the terms like Git, Gitlab, GitHub, and Bitbucket below:<br/>
- **Git**  - It is a source code versioning system that lets you locally track changes and push or pull changes from remote resources.
- **GitLab, GitHub, and Bitbucket** - Are service that provides remote access to Git repositories, hosting the source code and help manage the software development lifecycle.
- **Container registry** - Is a storage and content delivery system, which stores their Docker (it is database of predefined images used to run applications.) images.

<h3>Docker</h3>

Docker is an open platform for developing, shipping, and running applications see: <https://docs.docker.com/get-started/overview/>. 

**Docker Container**<br/>
A container is a sandboxed process on your machine that is isolated from all other processes on the host machine. That isolation leverages kernel namespaces and cgroups, features that have been in Linux for a long time. Docker has worked to make these capabilities approachable and easy to use. To summarize, a container:<br/>
- Is a runnable instance of an image. You can create, start, stop, move, or delete a container using the DockerAPI or CLI.
- Can be run on local machines, virtual machines or deployed to the cloud.
- Is portable (can be run on any OS).
- Is isolated from other containers and runs its own software, binaries, and configurations.<br/>

**Docker Image**<br/>
When running a container, it uses an isolated filesystem. This custom filesystem is provided by a container image. Since the image contains the container’s filesystem, it must contain everything needed to run an application - all dependencies, configurations, scripts, binaries, etc. The image also contains other configuration for the container, such as environment variables, a default command to run, and other metadata.<br/>

**Docker Network**<br/>
To **ease communication** between Docker container, that run on the same Docker host you need to **create a custom Docker network**.<br/>
Within this network the **name of a docker container serves as a DNS name**. this is not the case in the default Docker network.<br/>
The following command will create a *network bridge* with the *name test-network*.<br/>

**Dockerfile**<br/>
A Dockerfile is a descriptor to build Docker images.
This topic: <https://docs.docker.com/develop/develop-images/dockerfile_best-practices/> covers recommended best practices and methods for building efficient images

## Technology
- Java EE 10 <https://jakarta.ee/release/10/>
	- Provided a vast array of web components of new Jakarta namespace
- Java EE Server
	- Application platform for Jakarta EE (Java EE) apps (e.g. based on WildFly, Payara, GlassFish, WebLogic, WebSphere...)
- CDI 
	- Context and Dependency Injection as a standard framework as part of Java EE for creating containers and injecting dependencies (Java Beans)
- JSF 
	- Java Server Faces as a web application framework that uses facelets and allow building XHTML web-pages, templates, managing validation and navigation as well as communication with Java Beans (CDI)
- JPA 
	- Java Persistence API as a collection of classes and methods to persistently store the data into a database
   	- A bridge between object models (Java program) and relational models (database program)

## Components

<h3>Java EE 10</h3>

Web components of Jakarta namespace<br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
	<groupId>jakarta.platform</groupId>
	<artifactId>jakarta.jakartaee-api</artifactId>
	<version>${jakartaee-api.version}</version>
	<scope>provided</scope>
</dependency>
```

<h3>Java EE 10 Server</h3>

- **WildFly** is a modular and lightweight **Jakarta** based Java EE application server that helps build applications.<br/>
Download the server: <https://www.wildfly.org>

<h3>JPA Provider</h3>

- **Hibernate** implementation is used as JPA Provider <https://hibernate.org/>.

**Persistence-unit**<br/>
The connection properties of a databases can be configured as a Persistence-unit.<br/><br/>
Add the following ``persistence.xml`` file into MAVEN project under ``/src/main/resources/META-INF`` folder:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
	xmlns="http://xmlns.jcp.org/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
	
	<persistence-unit name="admin-jta" >
	
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		
		<!-- jta-data-source as jndi-name in the standalone.xml -->
		<jta-data-source>java:/AdminDS</jta-data-source>
		
    	<!-- Entities -->
		<class>br.com.xxx.User</class>
		<class>br.com.xxx...</class>
		
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
			<property name="hibernate.hbm2ddl.auto" value="update"/>
			<property name="hibernate.show_sql" value="true"/>
		</properties>
		
	</persistence-unit>
</persistence>
```
<h3>Database</h3>

- **PostgreSQL** is an open source object-relational database system.<br/>
Download the database system: <https://www.postgresql.org>.<br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
   <version>42.2.12</version>
</dependency>
```

- **Connection Pool**<br/>
**HikariCP** is a "zero-overhead" production ready JDBC connection pool.<br/>See more at: <https://github.com/brettwooldridge/HikariCP><br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
   <groupId>com.zaxxer</groupId>
   <artifactId>HikariCP</artifactId>
   <version>5.0.1</version>
</dependency>
```

<h3>Scheduling</h3>

- **Quartz** is a richly featured, open source job scheduling library: <http://www.quartz-scheduler.org/><br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
	<groupId>org.quartz-scheduler</groupId>
	<artifactId>quartz</artifactId>
	<version>${quartz.version}</version>
</dependency>
<dependency>
	<groupId>org.quartz-scheduler</groupId>
	<artifactId>quartz-jobs</artifactId>
	<version>${quartz.version}</version>
</dependency>
```

<h3>JSF-Framework</h3>

**PrimeFaces** 

PrimeFaces is a lightweight library to extend JSF components with one jar, zero-configuration and no required dependencies.<br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>12.0.0</version>
</dependency>
```


**Namespaces**

PrimeFaces namespace is necessary to add PrimeFaces components to the ``.xhtml`` pages.

```xml
<html xmlns:p="http://primefaces.org/ui">
</html>
```
How to get started quickly with PrimeFaces see: <https://www.mastertheboss.com/category/web/primefaces/>

**Font-Awesome**

PrimeFaces supports FontAwesome 4.x, 5.x, and 6.x+.<br/><br/>
Add the following into MAVEN project ``pom.xml`` file under ``<dependencies>`` element section:

```xml
<dependency>
     <groupId>org.webjars</groupId>
     <artifactId>font-awesome</artifactId>
     <version>5.12.0</version>
</dependency>
```

Then include these two CSS style sheets to enable FontAwesome:

```xml
<h:outputStylesheet library="webjars" name="font-awesome/5.12.0/css/all.min-jsf.css" />
<h:outputStylesheet library="webjars" name="font-awesome/5.12.0/cs
```

## Environment

<h3>Local environment</h3>

This project should be build for local development for WildFly application server with MAVEN as '.war' file:<br/>

- run MAVEN commands in the root folder of the Java project,<br/><br/>
``mvn clean``<br/>
``mvn initialize``<br/>
``mvn install -DskipTests``<br/><br/> 
 the ``.war`` file will be generated and stored in ``./target`` folder of the Java project.<br/>
 The ``initialize`` command is required to install the local jars from the source code to the MAVEN local repository.<br/>   
- copy the ``.war`` file to the deployments directory ``/opt/jboss/m/standalone/deployments/`` of **WildFly** server installation<br/> <br/> 
- start run **WildFly** server with: ``.\bin\standalone.bat`` file on windows OS or ``.\bin\standalone.sh`` on Linux OS<br/>  
- navigate on browser to: <https://localhost:8008/index.xhtml> to access the application

<h3>Prod environment</h3>

The target environment: **cr.cicd.skyway.porsche.com**

**Container registry**<br/>

The following **docker** commands have to be used to add the required images to the **Container registry** of target environment.

- download to local docker machine<br/>
 ``docker pull quay.io/wildfly/wildfly:27.0.0.Final-jdk17``

- change the host of the tag on local docker machine</h4>
 ``docker tag quay.io/wildfly/wildfly:27.0.0.Final-jdk17 [pathTo]/wildfly:27.0.0.Final-jdk17 ``
 
- login to docker machine of target environment<br/>
 ``docker login [login-host] -u {gitlab-user} -p {gitlab-password]``

- upload tagged-image to docker machine of target environment<br/>
 ``docker push [pathTo]/wildfly:27.0.0.Final-jdk17``

**Dockerfile**<br/><br/>

A Dockerfile is a descriptor to build Docker images.

```xml

# ++++++++++++++++++++++++++++++
# 1) Build release
# ++++++++++++++++++++++++++++++

# select image for maven build
FROM maven:3.8.4-openjdk-17-slim AS BUILD

# Set working directory of maven build
WORKDIR /tmp/

# Add files of maven multi module project to working directory

# Copy [pom] project
COPY pom.xml /tmp/

# Copy [main] project
COPY api /tmp/api/

# Copy [sub] projects
COPY app /tmp/app/
COPY inf /tmp/inf/

# Copy maven settings file
COPY settings.xml /tmp/


# Build the .war file with maven
RUN mvn initialize -P prod --settings settings.xml -DskipTests
RUN mvn clean install -P prod --settings settings.xml -DskipTests

# ++++++++++++++++++++++++++++++
# 2) Deploy release
# ++++++++++++++++++++++++++++++

# select image from 'container registry' for WildFly server
FROM cr.cicd.skyway.porsche.com/testmanager_x42/test-exchange-42/tex-42/wildfly:27.0.0.Final-jdk17

# Copy the .war file from working directory to the deployments directory of WildFly
COPY --from=BUILD /tmp/api/target/*.war /opt/jboss/wildfly/standalone/deployments/

# Add new admin user to wildfly server
RUN /opt/jboss/wildfly/bin/add-user.sh admin awagner --silent

# Set user
USER jboss

# Expose the ports
EXPOSE 8080

# Entrypoint for start command of wildfly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "--debug"]

```

## Deployment 

**Pipeline**
- GitLab


#### Docker development

In this prototype the DockerImage is created from a .jar. Therefore, we must run ``mvnw initialize install -DskipTests`` first.<br/>
The ``initialize`` command is required to install the local jars from the source code to the MAVEN local repository.  
It is required, that the .jar will be stored in ./target<br/>


To **launch the JSF-App as a Docker** container we need to **create a DockerImage*** from our application.<br/>
To do this, in your prefered shell, **navigate to** your app diretory, where the **Dockerfile** is stored.<br/>
Execute the following command, which will **build a DockerImage based on the configuration of the DockerFile**<br/><br/>
``docker build -t docker-demo .``

To deploy the app you need to **create a Docker container from the previous created DockerImage**.<br/>
A DockerImage serves as a blueprint for severall Docker containers.<br/>
To do that, execute the following command, which will create a *Docker container* and run it on *port 8080* with the *name jsf-con*,
in the previous created custom Docker network.<br/><br/>
``docker run --network test-network -p 8080:8080 --name jsf-con docker-demo``


## Troubleshoot
Each project pipeline in the test-exchange subgroup has a trigger to the troubleshoot pipeline. **To facilitate this trigger, the name of the docker-container has to match the project name!** To use the troubleshooting processes, head to the piepline section of the project. There you should see an expandeable pipeline job. Expand it and you should see all predefined troubleshoot processes. These processes are the most frequently used ones. Feel free to define new jobs in the troubleshooting project pipeline. Please consider the same style of pipeline jobs as the already defined ones.


## TODO

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://cicd.skyway.porsche.com/testmanager_x42/test-exchange-42/tex-42.git

git branch -M main
git push -uf origin main
```
