package tn.cloudnine.queute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Autorise toutes les routes
                        .allowedOrigins("http://localhost:4200","http://localhost:4300") // Autorise ton frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS") // Méthodes HTTP
                        .allowedHeaders("*") // Autorise tous les headers
                        .allowCredentials(true); // Autorise les cookies ou l'auth si besoin

            }
        };


    }

}