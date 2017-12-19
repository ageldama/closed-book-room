package jhyun.cbr.controllers;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jhyun.cbr.services.UserService;
import jhyun.cbr.storage.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api("사용자 로그인 정보를 위한 API")
@RequestMapping("/v1")
@RestController
public class UserController {
    private UserService userService;
    private SpringSecurityHelper springSecurityHelper;

    @Autowired
    public UserController(UserService userService, SpringSecurityHelper springSecurityHelper) {
        this.userService = userService;
        this.springSecurityHelper = springSecurityHelper;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "로그인 상태 아님"),
            @ApiResponse(code = 404, message = "사용자 정보 없음"),
    })
    @ApiOperation("현재 로그인한 사용자 정보 얻기")
    @RequestMapping(value = "/current-user", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> getCurrentUser() {
        final Optional<String> username = springSecurityHelper.getUsername();
        if (username.isPresent()) {
            final User user = userService.getUser(username.get());
            if (user == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(user, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
