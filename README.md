## Requirements

For building and running the application you need:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 4.5.1](https://gradle.org)

## Running the application locally

Run the application locally using the Gradle wrapper (gradlew)

```shell
./gradlew bootRun   (this will internally call the compile/build tasks)
```

Once the build completes, run the unit tests for the application and then use Postman to run API tests

## Testing the Application 
### Unit Tests
See the testing folder to find the list of Postman functional and load tests that can be run as:
```shell
./gradlew test
````

The tests output are located at:
```shell
build/reports/test
````
### Functional Tests
Use Postman (found at: https://www.getpostman.com) to run the pre-defined tests under:

```shell
test-scripts/
```

## API Documentation
Once the test are run, the documentation can be generated as:

```shell
./gradlew asciidoc
```

The documentation will be created under:

```shell
build/asciidoc/html5
```

## Deploying the application to OpenShift (not fully supported yet)

The easiest way to deploy the sample application to OpenShift is to use the [OpenShift CLI](https://docs.openshift.org/latest/cli_reference/index.html):

```shell
oc new-app <TBD>
```

