package com.ERP.erp.config;

import com.ERP.erp.user.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailService userDetailService;

    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // --- Complete the Security Filter Chain Configuration ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for simplicity in development.
                // RE-ENABLE and configure properly for production applications!
                .csrf(AbstractHttpConfigurer::disable)

                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(authorize -> authorize
                        // Allow unauthenticated access to specific paths (e.g., login page, static resources)
                        // IMPORTANT: Adjust "/login", "/static/**" etc. to match your actual public paths
                        .requestMatchers("/login", "/auth/**", "/register", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // If you have a public registration page, allow access to it:
                        // .requestMatchers("/register").permitAll()

                        // Require users with the "ADMIN" role to access paths starting with "/admin"
                        // Note: Spring Security automatically adds "ROLE_" prefix if using hasRole(),
                        // so store roles in DB as "ROLE_ADMIN", "ROLE_USER" etc.
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Require users with either "ADMIN" or "MANAGER" role for paths under "/manage"
                        // .requestMatchers("/manage/**").hasAnyRole("ADMIN", "MANAGER")

                        // Require any authenticated user to access all other paths
                        .anyRequest().authenticated()
                )

                // Configure Form-Based Login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // Configure Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}