package tn.cloudnine.queute.config.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser  = (CustomOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getName();
        String redirectUrl = "http://localhost:4200/auth/oauth2-redirect?email=" +
                URLEncoder.encode(email, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);


    }
}
