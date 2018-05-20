## Description
This is a small project to provide a RESTful API for gathering data for IoT devices. With this API, you can:
- Create, read, delete configuration data for IoT devices
- Store temperature data (collected every second)
- Retrieve the last minute, last hour, last day, last week, last month, and last year worth of data.

Design approach: 
In order to support an API that could scale both in throughput and data, MongoDB was chosen as the data store since its document-store structure provide a natural fit for time-series data. In particular, all of the data for a particular device can be retrieved in a single document fetch and sub-document arrays (JSON sub arrays) can be used to differentiate the different time periods so that retrieving particular groups of data (i.e day/month/year) can be done quite efficiently. Retrieval of data for each interval is done using MongoDB's aggregation pipeline framework. Additionally, the temperature data is stored in a separate collection from the device configuration data to highlight independence of data and allowing the time-series data to grow and not affect configuration data retrievals.

The middleware is Java using Spring Boot, Jackson, and other associated libraries.

## Requirements

For building and running the application you need:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 4.5.1](https://gradle.org)
- [MongoDB 3.6](https://www.mongodb.com)

## Running the application locally

Run the application locally using the Gradle wrapper (gradlew)

First, detect which environment you want to use (dev, openshift, prod, windowsdev). Each of these specific configurations are defined under src/main/environment/<env name>

- dev : meant for running on your local Linux/Mac (connects to MongoDB that assumed to be running and bound to localhost)
- windowsdev : meant for running on a local Windows PC
- openshift : meant for running in an OpenShift deployment (Note: currently under test and not yet fully deployed)
- prod : for future production configuration

The default environment is: dev

Linux or MacOS:
```shell
./gradlew -Penv=<env name> bootRun   (this will internally call the compile/build tasks)
```

Windows:
```shell
gradlew.bat -Penv=<env name> bootRun   (this will internally call the compile/build tasks)
```

Once the build completes, run the unit tests for the application and then use Postman to run API tests

## Testing the Application 
### Unit Tests
See the testing folder to find the list of Postman functional and load tests that can be run as:
```shell
./gradlew -Penv=<env name> test
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

