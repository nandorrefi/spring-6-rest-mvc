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