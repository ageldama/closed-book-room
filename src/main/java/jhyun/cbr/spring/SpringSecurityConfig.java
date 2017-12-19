package jhyun.cbr.spring;

import jhyun.cbr.storage.entities.UserRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/js/*").permitAll()
                // Swagger UI
                .antMatchers(
                        "/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagge‌​r-ui.html"
                ).permitAll()
                // Codes
                .antMatchers("/v1/codes/**").permitAll()
                // Admin API
                .antMatchers("/v1/admin/**").hasAuthority(UserRole.ROLE_ADMIN.getValue())
                //
                .anyRequest().authenticated();
        http.csrf().disable();  // NOTE: for ajax-login.
        //
        http.httpBasic();
    }
}
