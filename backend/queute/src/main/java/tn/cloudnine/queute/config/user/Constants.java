package tn.cloudnine.queute.config.user;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Configuration

public class Constants {
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();
    }
}
