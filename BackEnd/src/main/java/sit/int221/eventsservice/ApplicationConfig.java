package sit.int221.eventsservice;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import sit.int221.eventsservice.services.ListMapper;

@Configuration
public class ApplicationConfig {
    public ApplicationConfig() {
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(16, 29, 2, 16, 2);
    }
}
