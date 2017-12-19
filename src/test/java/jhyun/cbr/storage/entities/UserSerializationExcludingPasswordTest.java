package jhyun.cbr.storage.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSerializationExcludingPasswordTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void t() throws IOException {
        final User user = new User(123L, "username", "password", UserRole.ROLE_USER, true);
        final String jsonStr = objectMapper.writeValueAsString(user);
        assertThat(jsonStr).doesNotContain("password");
        //
        final Map<String, Object> map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        assertThat(map).doesNotContainKeys("password");
    }
}
