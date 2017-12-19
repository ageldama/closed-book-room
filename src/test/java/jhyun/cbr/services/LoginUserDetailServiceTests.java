package jhyun.cbr.services;

import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.entities.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserDetailServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginUserDetailService testSubject;

    @Test
    public void loadUserByUsername_ok() throws Exception {
        final String username = "myuser";
        // record mock
        when(userService.getUser(anyString())).then(
                invocation -> new User(1234L, username, "", UserRole.ROLE_USER, true));
        //
        final UserDetails user = testSubject.loadUserByUsername(username);
        // verify
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_notFound() throws Exception {
        // record: no user entries
        when(userService.getUser(anyString())).thenReturn(null);
        //
        testSubject.loadUserByUsername("this-user-should-not-exist");
    }

    @Test(expected = LockedException.class)
    public void loadUserByUsername_locked() throws Exception {
        // record: deactivated user
        when(userService.getUser(anyString())).then(
                invocation -> new User(1234L, "foobar", "", UserRole.ROLE_USER, false));
        //
        testSubject.loadUserByUsername("foobar");
    }
}