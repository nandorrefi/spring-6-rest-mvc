## Spring DevTools
### Setting up Spring auto-reload in IntelliJ

1. Add spring-boot-devtools as maven dependency
2. In IntelliJ community edition set make sure to check the checkbox in Settings -> Build, Execution, Deployment -> Compiler -> Build project automatically
3. Now if you run the spring application, modify a file and recompile/rebuild that file or the entire project then the devtools will automatically rerun the Spring application

### Notes

Dev tools provides useful features for development. For example it attaches the stack trace to failed responses.

These features are a security concern in production, but thankfully devtools are automatically disabled when the application gets launched from a jar or a special classloader.

Exact quote from the 3.3.5 [Spring documentation](https://docs.spring.io/spring-boot/reference/using/devtools.html#page-title):
> Developer tools are automatically disabled when running a fully packaged application. If your application is launched from java -jar or if it is started from a special classloader, then it is considered a “production application”. You can control this behavior by using the spring.devtools.restart.enabled system property. To enable devtools, irrespective of the classloader used to launch your application, set the -Dspring.devtools.restart.enabled=true system property. This must not be done in a production environment where running devtools is a security risk. To disable devtools, exclude the dependency or set the -Dspring.devtools.restart.enabled=false system property.

## Jayway JsonPath

https://github.com/json-path/JsonPath

## Spring Data JPA

**Java Persistence API (JPA)**: It is a Java specification that gives some functionality and standard to ORM tools.

**Hibernate**: A Java ORM framework that simplifies the interaction with the database. An implementation of the JPA specification.

### Data Transfer Objects (DTOs)

POJOs with NO behavior

DTOs only used for transferring data between producers and consumers

![dto-entity](docs/dtos-and-entities.png "HTTP requests")

### Why not use entities as DTOs?

In a more complex application it is important to separate the DB data from the API response data

Entities represent the tables in the database, we usually don't want to return every column from a table to the client

Consumers have different needs than the needs of persistence

### Type Conversions

Type Conversions are often done within methods but it is **best practice** to use dedicated converter components

Spring Framework provides a Converter interface with generics, can be used with our dedicated converter component

[MapStruct](https://mapstruct.org/documentation/reference-guide/) is a code generator which automates generation of type converters, kinda like Lombok