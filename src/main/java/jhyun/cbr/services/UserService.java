package jhyun.cbr.services;

import com.google.common.base.Strings;
import jhyun.cbr.storage.entities.User;
import jhyun.cbr.storage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class UserService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(final String username) {
        checkArgument(!Strings.isNullOrEmpty(username));
        //
        return userRepository.findOneByUsername(username);
    }

    @Transactional
    public User setUserActivation(final String username, final boolean activeness) {
        checkArgument(!Strings.isNullOrEmpty(username));
        //
        final User user = userRepository.findOneByUsername(username);
        if (user == null) {
            return null;
        } else {
            user.setActive(activeness);
            return userRepository.save(user);
        }
    }

    @Transactional
    public User setPassword(final String username, final String newPassword) {
        checkArgument(!Strings.isNullOrEmpty(username));
        checkArgument(!Strings.isNullOrEmpty(newPassword));
        //
        final User user = userRepository.findOneByUsername(username);
        if (user == null) {
            return null;
        } else {
            final String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            return userRepository.save(user);
        }
    }

    @Transactional
    public User saveNew(final User user) {
        checkArgument(user != null);
        return userRepository.save(user);
    }
}
