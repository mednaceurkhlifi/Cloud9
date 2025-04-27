package tn.cloudnine.queute.config.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tn.cloudnine.queute.service.user.JwtService;
import tn.cloudnine.queute.service.user.UserServiceImpl;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserServiceImpl oAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public SecurityConfig(OAuth2UserServiceImpl oAuth2UserService,
                          CustomOAuth2SuccessHandler customOAuth2SuccessHandler
            , JwtService jwtService, UserServiceImpl userServiceImpl) {
        this.customOAuth2SuccessHandler=customOAuth2SuccessHandler;
        this.oAuth2UserService = oAuth2UserService;
        this.jwtService=jwtService;
        this.userServiceImpl=userServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/User/retrieveAll",
                                "/User/findByEmail/**",
                                "/User/sign-in",
                                "/User/addUser",
                                "/User/reset-password-request",
                                "/User/reset-password",
                                "/public/**",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/api/v1/**",
                                "**"
                        ).permitAll().requestMatchers("/User/current").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionFixation().none()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                )
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            var error = Map.of("error", "Unauthorized access");
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        };
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtService, userServiceImpl);
    }

}