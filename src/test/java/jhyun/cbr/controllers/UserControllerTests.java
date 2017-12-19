package jhyun.cbr.controllers;

import jhyun.cbr.services.UserService;
import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.entities.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_noSuchUser;
import static jhyun.cbr.controllers.SpringSecurityHelperMocks.recordSpringSecurityHelper_ok;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {
    @Mock
    private UserService userService;

    @Mock
    private SpringSecurityHelper springSecurityHelper;

    @InjectMocks
    private UserController testSubject;

    private void recordUserService_getUser_noSuchUser() {
        when(userService.getUser(anyString()))
                .then(invocationOnMock -> null);
    }

    private void recordUserService_getUser_ok() {
        when(userService.getUser(anyString()))
                .then(invocationOnMock -> {
                    final User it = new User();
                    it.setPassword("foobarpassword");
                    it.setId(1234L);
                    it.setRole(UserRole.ROLE_USER);
                    it.setUsername(Objects.toString(invocationOnMock.getArguments()[0]));
                    return it;
                });
    }

    // `getCurrentUser`
    @Test
    public void getCurrentUser_ok() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<String> usernameFuture = recordSpringSecurityHelper_ok(springSecurityHelper);
        recordUserService_getUser_ok();
        //
        final ResponseEntity<User> response = testSubject.getCurrentUser();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final String generatedUsername = usernameFuture.get(10, TimeUnit.MILLISECONDS);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(generatedUsername);
        // NOTE: password omitting should be done and it has done with `UserSerializationExcludingPasswordTest`, not here.
        // verify mocks
        verify(springSecurityHelper, times(1))
                .getUsername();
        verify(userService, times(1))
                .getUser(eq(generatedUsername));
    }

    @Test
    public void getCurrentUser_notLoggedIn() {
        recordSpringSecurityHelper_noSuchUser(springSecurityHelper);
        recordUserService_getUser_noSuchUser();
        //
        final ResponseEntity<User> response = testSubject.getCurrentUser();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        // verify mocks
        verify(springSecurityHelper, times(1))
                .getUsername();
        verify(userService, times(0))
                .getUser(anyString());
    }

    @Test
    public void getCurrentUser_noSuchUser() {
        recordSpringSecurityHelper_ok(springSecurityHelper);
        recordUserService_getUser_noSuchUser();
        //
        final ResponseEntity<User> response = testSubject.getCurrentUser();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        // verify mocks
        verify(springSecurityHelper, times(1))
                .getUsername();
        verify(userService, times(1))
                .getUser(anyString());
    }
}