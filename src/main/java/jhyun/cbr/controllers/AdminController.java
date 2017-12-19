package jhyun.cbr.controllers;

import com.github.javafaker.Faker;
import io.swagger.annotations.*;
import jhyun.cbr.services.UserService;
import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.entities.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api("사용자 관리 API")
@RequestMapping("/v1/admin")
@RestController
public class AdminController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserService userService;
    private Faker faker = new Faker();

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "사용자 추가 (`USER`-role만 지원)", code = 201)
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED with Random Password and Deactivated"),
    })
    @RequestMapping(value = "/new-user/{username}", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(
            @ApiParam(value = "새로 생성한 사용자 ID", required = true)
            @PathVariable("username")
                    String username
    ) {
        final String newPassword = faker.pokemon().name() + "-" + faker.gameOfThrones().character();
        final User user = new User(null, username, newPassword, UserRole.ROLE_USER, false);
        LOGGER.debug("NEW USER = {}", user);
        final User savedUser = userService.saveNew(user);
        LOGGER.debug("SAVED NEW USER = {}", savedUser);
        savedUser.setPassword("---REMOVED---");
        return new ResponseEntity(savedUser, HttpStatus.CREATED);
    }

    @ApiOperation("사용자 활성화/비활성화")
    @ApiResponses({
            @ApiResponse(code = 200, message = "UPDATED"),
    })
    @RequestMapping(value = "/activate-user/{username}",
            method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> activateUser(
            @ApiParam(value = "사용자 ID", required = true)
            @PathVariable(value = "username")
                    String username,
            @ApiParam(value = "활성화=`true`/비활성화=`false`", required = true)
            @RequestParam(value = "activeness")
                    Boolean activeness) {
        final User result = userService.setUserActivation(username, activeness);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "사용자 비밀번호 변경", code = 204)
    @ApiResponses({
            @ApiResponse(code = 204, message = "UPDATED, NO CONTENT"),
    })
    @RequestMapping(value = "/user-password/{username}",
            method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setPassword(
            @ApiParam(value = "사용자 ID", required = true)
            @PathVariable("username")
                    String username,
            @ApiParam(value = "새 비밀번호(cleartext)", required = true)
            @RequestBody
                    String newPassword
    ) {
        final User result = userService.setPassword(username, StringUtils.trim(newPassword));
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
