package jhyun.cbr.controllers;

import jhyun.cbr.controllers.endpoint_secured_ensuring.WithMockRoleUser;
import jhyun.cbr.controllers.http_status_exceptions.NotAuthorizedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityHelperTests {

    @Autowired
    private SpringSecurityHelper testSubject;

    @Test
    public void getUsername_empty() {
        final Optional<String> result = testSubject.getUsername();
        assertThat(result.isPresent()).isFalse();
    }

    @WithMockRoleUser
    @Test
    public void getUsername_ok() {
        final Optional<String> result = testSubject.getUsername();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotEmpty();
    }

    @WithMockRoleUser
    @Test
    public void getUsernameOrThrow_ok() {
        final String username = testSubject.getUsernameOrThrow();
        assertThat(username).isNotEmpty();
    }

    @Test(expected = NotAuthorizedException.class)
    public void getUsernameOrThrow_throws() {
        testSubject.getUsernameOrThrow();
    }

}
