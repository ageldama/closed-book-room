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

## 사용한 오픈소스 라이브러리
1. Spring Boot
1. Flyway : Hibernate DDL 자동생성 대신에 직접 스키마 생성하고 마이그레이션 가능하도록 적용.
1. Spring Security : 로그인 기능을 위해 사용
1. Guava, Commons-io, Commons-lang3 : Java 기본 라이브러리로 사용
1. SpringFox Swagger2 : Swagger2 
1. Javafaker : 간단히 랜덤 아이디, 이메일 등 생성하기 위해 사용
1. JaCoCo : Test Code Coverage Report 생성위해 Maven 플러그인으로 사용
1. HSQLDb : 애플리케이션 디비로 인메모리 디비 구현으로 사용

