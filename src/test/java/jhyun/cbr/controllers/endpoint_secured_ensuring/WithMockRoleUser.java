package jhyun.cbr.controllers.endpoint_secured_ensuring;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@WithMockUser(username = "test", password="test", authorities = {"ROLE_USER"})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithMockRoleUser {
}
