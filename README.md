# Market Data Tracker

## Description
Application for monitoring cryptocurrency orders available in Kraken.

## Before You Start
### Dependencies Set-Up
The project is currently configured to run on macOS with Apple's ARM architecture.

While it should also work as is with Intel CPUs and other operating systems, in these cases it is still advised to replace the contents of `build.gradle` with that of `build.intel.gradle`*, to avoid any potential problems.

_*I.e. remove the transitive dependency `netty-resolver-dns-native-macos` override._

### Performance
All provided Docker scripts that do **_not_** require a local JDK will set up Gradle inside the application container, which is a process that takes a while.

That being said, caching has been enabled, so experience should improve in subsequent iterations.

### Commands & Scripts
All commands & scripts listed here **_must_** be executed from the **_project's root directory_**.

### Customisation
To change the maximum number of orders received per Kraken message, modify the `kraken.order.count.max` property in `src/main/resources/application.yaml`*.

Note, as per [Kraken's documentation](https://docs.kraken.com/websockets/#message-subscribe) at the time of writing, the only valid options are, as follows: 
> 10, 25, 100, 500, 1000

_*If you are using `run-app-with-local-jdk.sh`, then you need to change the `KRAKEN_ORDER_MAX_COUNT` environment variable in `docker/.env` instead._

### Development Set-Up
#### Lombok
The project uses [Lombok](https://projectlombok.org/), so Lombok's annotation processing plugin will be needed when working with an IDE or a code editor.

#### Formatting
[Prettier Java](https://github.com/jhipster/prettier-java) & [CheckStyle](https://checkstyle.sourceforge.io/) have been added for formatting purposes.

Apart from the links above, Prettier's [README](https://github.com/jhipster/prettier-java#install-prettier-and-prettier-java-plugin) & [Advanced Usage](https://github.com/jhipster/prettier-java/blob/main/docs/advanced_usage.md#ide-integrations) documents should serve as a sufficient reference on how to achieve the integration with your set-up.

## How to Run

### Application
After having made sure the project's dependencies are suitable for your machine (see the _Dependencies Set-Up_ section above), either of the following commands can be used in order to start the application:
* **without** Docker:
  * with [a compatible version](https://docs.gradle.org/current/userguide/compatibility.html) of **Gradle** (>=7.6): `gradle clean bootRun`;
  * with just **JDK** (>=19): `./gradlew clean bootRun`;
* **with** Docker:
  * plus a **local JDK >=19**: `./docker/scripts/run-app-with-local-jdk.sh`;
  * and nothing else (no local JDK >=19 required): `./docker/scripts/run-app-no-local-jdk.sh`.

### Tests
Either of the following commands can be used to run the project's tests:
* **without** Docker:
  * with [a compatible version](https://docs.gradle.org/current/userguide/compatibility.html) of **Gradle** (>=7.6): `gradle test`;
  * with just **JDK** >=19: `./gradlew test`;
* **with** Docker (no local JDK >=19 required)—inside a container that uses the host's project directory: `./docker/scripts/run-tests.sh`.

### Test Coverage
Either of the following commands can be used to generate a test coverage report:
* **without** Docker:
  * with [a compatible version](https://docs.gradle.org/current/userguide/compatibility.html) of **Gradle** (>=7.6): `gradle jacocoTestReport`;
  * with just **JDK** >=19: `./gradlew jacocoTestReport`;
* **with** Docker (no local JDK >=19 required)—inside a container that uses the host's project directory & outputs to it: `./docker/scripts/run-coverage.sh`.

The report can then be viewed in HTML format by opening `build/reports/jacoco/test/html/index.html`.

### Docker Clean-Up
As the `run-` shells scripts result in running Docker containers in "attached" mode, the following `rm-` scripts can be used to release the allocated resources:
* containers & volumes: `./docker/scripts/rmcv.sh`;
* images: `./docker/scripts/rmi.sh`;
* everything above: `./docker/scripts/rme.sh`.

## Further Readings
[Kraken Websockets API](https://docs.kraken.com/websockets/)
