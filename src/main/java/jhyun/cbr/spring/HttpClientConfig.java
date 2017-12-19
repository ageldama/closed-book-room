package jhyun.cbr.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Scope("prototype")
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
