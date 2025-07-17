
package com.demo.demo.config;

import com.demo.demo.service.AuthenticationService;
import com.demo.demo.service.CustomOAuth2UserService;
import com.demo.demo.service.OAuth2LoginSuccessHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private OAuth2LoginSuccessHandler successHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**",
                                "/login/**",

                                "/oauth2/**").permitAll()
                        // a) Cho phép tài nguyên tĩnh: CSS, JS, hình, favicon...
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // b) Cho phép truy cập file HTML chat demo
                        .requestMatchers(HttpMethod.GET, "/chat.html", "/", "/index.html").permitAll()

                        // c) Cho phép đăng nhập/đăng ký
                        .requestMatchers(HttpMethod.POST, "/api/signin", "/api/register").permitAll()

                        // d) Cho phép endpoint WebSocket/SockJS
                        .requestMatchers("/ws/**", "/sockjs-client/**").permitAll()

                        // e) Cho phép OpenAPI/Swagger nếu dùng
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // f) Các REST API đọc lịch sử chat (yêu cầu JWT)
                        .requestMatchers(HttpMethod.GET, "/api/chat/history").authenticated()

                        // g) Các API khác bắt buộc xác thực
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("${payos.webhook-path}").permitAll()
                        .anyRequest()
                        .authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//     @Bean
// public ClientRegistrationRepository clientRegistrationRepository() {
//     return new InMemoryClientRegistrationRepository(
//         ClientRegistration.withRegistrationId("google")
//             .clientId("your-client-id")
//             .clientSecret("your-client-secret")
//             .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//             .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//             .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//             .scope("email", "profile", "https://www.googleapis.com/auth/user.birthday.read")
//             .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//             .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//             .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//             .userNameAttributeName(IdTokenClaimNames.SUB)
//             .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//             .clientName("Google")
//             .build()
//     );}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));            // hoặc List.of("http://localhost:3000")
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}