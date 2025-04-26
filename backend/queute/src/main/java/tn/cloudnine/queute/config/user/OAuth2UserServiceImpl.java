package tn.cloudnine.queute.config.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.cloudnine.queute.enums.roles.Role;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public OAuth2UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        String resolvedEmail = oAuth2User.getAttribute("email");
        String resolvedFirstName = oAuth2User.getAttribute("given_name");
        String resolvedLastName = oAuth2User.getAttribute("family_name");

        if ("github".equalsIgnoreCase(registrationId)) {
            resolvedFirstName = oAuth2User.getAttribute("name"); // GitHub gives full name
            resolvedLastName = "";
            if (resolvedEmail == null) {
                resolvedEmail = fetchEmailFromGitHubApi(userRequest.getAccessToken().getTokenValue());
            }
        }

        if (resolvedEmail == null || resolvedFirstName == null || resolvedLastName == null) {
            throw new OAuth2AuthenticationException("Missing user information from OAuth2 provider");
        }

        Random random = new Random();
        int randomInt = 1000 + random.nextInt(9000);
        String finalEmail = resolvedEmail;
        String finalFirstName = resolvedFirstName;
        String finalLastName = resolvedLastName;

        User user = userRepository.findByEmail(finalEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(finalEmail);
            newUser.setFirstName(finalFirstName);
            newUser.setLastName(finalLastName);
            newUser.setLogin(finalFirstName.toLowerCase() + "-" + randomInt);
            newUser.setPassword("");
            newUser.setRole(Role.CLIENT);
            newUser.setCreated_at(LocalDateTime.now());
            newUser.setEnabled(true);
            return userRepository.save(newUser);
        });

        return new CustomOAuth2User(oAuth2User, user);
    }


    private String fetchEmailFromGitHubApi(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );


        if (response.getBody().isEmpty()) {
            System.out.println("GitHub email fetch failed or response is empty.");
            return null;
        }

        for (Map<String, Object> emailEntry : response.getBody()) {
            if (Boolean.TRUE.equals(emailEntry.get("primary")) && Boolean.TRUE.equals(emailEntry.get("verified"))) {
                return (String) emailEntry.get("email");
            }
        }

        System.out.println("No verified email found for GitHub user.");
        return null;
    }
}