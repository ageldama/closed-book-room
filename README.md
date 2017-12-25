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
    1. Default Admin: ID=`admin` Password=`admin`
    1. If you really needs to add new admin account, please add an entry in `./src/main/resources/db/migration/hsqldb/V1.001__create_users.sql` like one for `admin`. (Currently adding admin account through API-endpoint is not supported)

### Add an user
1. `curl -v -XPOST http://admin:admin@localhost:8080/v1/admin/new-user/foobar` : creates new deactivated user account with random-generated-password.
1. `curl -v -XPATCH http://admin:admin@localhost:8080/v1/admin/activate-user/foobar\?activeness\=true` : activate account.
1. `curl -v -XPOST http://admin:admin@localhost:8080/v1/admin/user-password/foobar -dfoobar -H"Content-Type: text/plain"` : set password
