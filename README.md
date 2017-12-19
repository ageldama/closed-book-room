# closed-book-room

## Requirements
1. Java 1.8+
1. Maven 3+
1. Modern Web browser : because web interfaces are implemented with HTML5 / Vue.js

## Run tests
1. `mvn test`
1. To check test coverage: open `./target/jacoco-ut/index.html` in web browser.

## Run standalone
1. `mvn spring-boot:run`
1. Open `http://localhost:8080` in your web browser.
    1. Default User: ID=`test` Password=`test`.

## API definitions
1. (Run `mvn spring-boot:run`)
1. Open `http://localhost:8080/swagger-ui.html` to check Swagger-UI.

## Admin endpoints
1. Can create new user, change password of user and activate/deactive user account,
    1. See `admin-controller`-section of Swagger definition.
