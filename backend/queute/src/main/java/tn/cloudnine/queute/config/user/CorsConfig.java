//package tn.cloudnine.queute.config.user;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//    @Bean
//    public CorsFilter corsFilter(){
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("*"));
//        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Authorization"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",config);
//        return new CorsFilter(source);
//    }
//}


package tn.cloudnine.queute.config.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();

        // Use allowedOriginPatterns instead of allowedOrigins
        config.setAllowedOriginPatterns(List.of("*"));  // ✅ Allows wildcard origins with credentials
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);  // ✅ Now this will work
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}