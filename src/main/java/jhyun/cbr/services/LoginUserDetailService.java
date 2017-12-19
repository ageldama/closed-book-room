package jhyun.cbr.services;

import jhyun.cbr.storage.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public LoginUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("for [%s]", username));
        } else if (user.getActive() == null || user.getActive() == false) {
            throw new LockedException(String.format("User [%s] is locked", username));
        } else {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole().getValue()));
        }
    }
}
