package jhyun.cbr.services;

import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.entities.UserRole;
import jhyun.cbr.storage.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static jhyun.cbr.testing_supports.ThrownByHelper.assertThrownByPairs;
import static jhyun.cbr.testing_supports.ThrownByHelper.thrownByPair;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService testSubject;

    private Future<User> recordUserRepositoryWithActiveUser() {
        final CompletableFuture<User> userFuture = new CompletableFuture<>();
        when(userRepository.findOneByUsername(anyString()))
                .then(invocationOnMock -> {
                    final User it = new User(1234L,
                            Objects.toString(invocationOnMock.getArguments()[0]),
                            "", UserRole.ROLE_USER, true);
                    userFuture.complete(it);
                    return it;
                });
        return userFuture;
    }

    private void recordUserRepositoryAlwaysNullForAnyUsername() {
        when(userRepository.findOneByUsername(anyString()))
                .thenReturn(null);
    }

    private void recordUserRepositorySaveOk() {
        when(userRepository.save(any(User.class)))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    private void recordPasswordEncoderEncodeNoop() {
        when(passwordEncoder.encode(anyString()))
                .then(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    // `getUser`

    @Test
    public void getUser_ok() throws Exception {
        // record mock
        recordUserRepositoryWithActiveUser();
        //
        final User user = testSubject.getUser("foobar");
        // verify
        assertThat(user).isNotNull();
        // verify mock
        verify(userRepository, times(1))
                .findOneByUsername(anyString());
    }

    @Test
    public void getUser_notFoundReturnsNull() {
        // record mock
        recordUserRepositoryAlwaysNullForAnyUsername();
        //
        final User user = testSubject.getUser("foobar-whatever");
        assertThat(user).isNull();
        // verify mock
        verify(userRepository, times(1))
                .findOneByUsername(anyString());
    }

    @Test
    public void getUser_rejectsSomeUsernames() {
        assertThrownByPairs(
                thrownByPair("null-username",
                        () -> testSubject.getUser(null),
                        IllegalArgumentException.class),
                thrownByPair("empty-username",
                        () -> testSubject.getUser(""),
                        IllegalArgumentException.class)
        );
    }

    // `setUserActivation`

    @Test
    public void setUserActivation_ok() throws Exception {
        // record mocks
        final Future<User> recordedUserFuture = recordUserRepositoryWithActiveUser();
        recordUserRepositorySaveOk();
        //
        final User savedUser = testSubject.setUserActivation("foobar", true);
        // verify
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getActive()).isTrue();
        // verify mocks
        final ArgumentCaptor<String> usernameFindOneByUsernameCaptor =
                ArgumentCaptor.forClass(String.class);
        verify(userRepository, times(1))
                .findOneByUsername(usernameFindOneByUsernameCaptor.capture());
        assertThat(usernameFindOneByUsernameCaptor.getValue())
                .isEqualTo(savedUser.getUsername())
                .isEqualTo(recordedUserFuture.get(10, TimeUnit.MILLISECONDS).getUsername());
    }

    @Test
    public void setUserActivation_notFound() throws Exception {
        // record mock
        recordUserRepositoryAlwaysNullForAnyUsername();
        recordUserRepositorySaveOk();   // this should not be called
        //
        final User user = testSubject.setUserActivation("foobar", false);
        assertThat(user).isNull();
        // verify mock
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void setUserActivation_rejectsSomeUsernames() throws Exception {
        assertThrownByPairs(
                thrownByPair("null-username",
                        () -> testSubject.setUserActivation(null, true),
                        IllegalArgumentException.class),
                thrownByPair("empty-username",
                        () -> testSubject.setUserActivation("", true),
                        IllegalArgumentException.class)
        );
    }

    // `setPassword`

    @Test
    public void setPassword_ok() throws Exception {
        // record mocks
        recordUserRepositoryWithActiveUser();
        recordUserRepositorySaveOk();
        recordPasswordEncoderEncodeNoop();
        //
        final String username = "foobar";
        final String password = "password!";
        final User savedUser = testSubject.setPassword(username, password);
        // verify mocks
        verify(userRepository, times(1))
                .findOneByUsername(eq(username));
        final ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1))
                .save(savedUserCaptor.capture());
        assertThat(savedUserCaptor.getValue().getUsername())
                .isEqualTo(savedUser.getUsername())
                .isEqualTo(username);
        verify(passwordEncoder, times(1))
                .encode(eq(password));
    }

    @Test
    public void setPassword_notFound() {
        // record mock
        recordUserRepositoryAlwaysNullForAnyUsername();
        recordUserRepositorySaveOk();   // this should not be called
        //
        final User it = testSubject.setPassword("foobar", "password!");
        assertThat(it).isNull();
        // verify mock
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void setPassword_rejectsSomeArgs() {
        assertThrownByPairs(
                thrownByPair("null-username",
                        () -> testSubject.setPassword(null, "1234"),
                        IllegalArgumentException.class),
                thrownByPair("empty-username",
                        () -> testSubject.setPassword("", "1234"),
                        IllegalArgumentException.class),
                thrownByPair("null-password",
                        () -> testSubject.setPassword("foobar", null),
                        IllegalArgumentException.class),
                thrownByPair("empty-password",
                        () -> testSubject.setPassword("foobar", ""),
                        IllegalArgumentException.class)
        );
    }

    // `saveNew`

    @Test
    public void saveNew() {
        recordUserRepositorySaveOk();
        //
        final User saved = testSubject.saveNew(new User(1234L, "foobar", "password",
                UserRole.ROLE_USER, true));
        assertThat(saved).isNotNull();
        //
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isNotNull();
        assertThat(userCaptor.getValue()).isEqualTo(saved);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNew_rejectsNullArg() {
        testSubject.saveNew(null);
    }

}