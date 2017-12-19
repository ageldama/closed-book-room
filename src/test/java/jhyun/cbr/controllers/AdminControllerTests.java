package jhyun.cbr.controllers;

import jhyun.cbr.services.UserService;
import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.entities.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController testSubject;

    @Test
    public void createNewUser_ok() {
        when(userService.saveNew(any(User.class)))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);
        //
        final String username = "foobar";
        final ResponseEntity<User> response = testSubject.createNewUser(username);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        //
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).saveNew(userCaptor.capture());
        final User user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getActive()).isFalse();
        assertThat(user.getRole()).isEqualTo(UserRole.ROLE_USER);
    }

    @Test
    public void activateUser_ok() {
        when(userService.setUserActivation(anyString(), anyBoolean()))
                .then(invocationOnMock -> {
                    final String username = Objects.toString(invocationOnMock.getArguments()[0]);
                    final Boolean activeness = (Boolean) invocationOnMock.getArguments()[1];
                    final User it = new User(1234L, username, "password", UserRole.ROLE_USER, activeness);
                    return it;
                });
        //
        final String username = "foobar";
        final ResponseEntity<User> response = testSubject.activateUser(username, true);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(username);
        assertThat(response.getBody().getActive()).isTrue();
        //
        verify(userService, times(1))
                .setUserActivation(eq(username), eq(true));
    }

    @Test
    public void activateUser_userNotFound() {
        when(userService.setUserActivation(anyString(), anyBoolean()))
                .then(invocationOnMock -> null);
        //
        final ResponseEntity<User> response = testSubject.activateUser("foobar", true);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void setPassword_ok() {
        when(userService.setPassword(anyString(), anyString()))
                .then(invocationOnMock -> {
                    final String username = Objects.toString(invocationOnMock.getArguments()[0]);
                    final String password = Objects.toString(invocationOnMock.getArguments()[1]);
                    final User it = new User(1234L, username, password, UserRole.ROLE_USER, false);
                    return it;
                });
        //
        final String username = "foobar";
        final String password = "password";
        final ResponseEntity<String> response = testSubject.setPassword(username, password);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        //
        verify(userService, times(1))
                .setPassword(eq(username), eq(password));
    }

    @Test
    public void setPassword_userNotFound() {
        when(userService.setPassword(anyString(), anyString()))
                .then(invocationOnMock -> null);
        //
        final ResponseEntity<String> response = testSubject.setPassword("foobar", "password");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}