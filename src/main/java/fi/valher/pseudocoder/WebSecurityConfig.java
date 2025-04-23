package fi.valher.pseudocoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fi.valher.pseudocoder.model.AppUser;
import fi.valher.pseudocoder.repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final UserRepository userRepository;

    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .requiresChannel(channel -> 
                channel.anyRequest().requiresSecure() // Enforce HTTPS in production
            )
            .cors(cors -> cors.and()) // Enable CORS support
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (adjust as needed)
            .authorizeHttpRequests(authorize -> 
                authorize
                    // Allow access to static resources
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/favicon.ico", "/index.html", "/assets/**").permitAll()
                    // Permit OPTIONS requests (for CORS preflight)
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // Allow API endpoints without authentication
                    .requestMatchers("/api/**").permitAll()
                    // Allow access to the root and fallback routes
                    .requestMatchers("/", "/{path:[^\\.]*}").permitAll()
                    // Protect all other endpoints
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin -> 
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/pseudoCodes", true)
                    .permitAll()
            )
            .logout(logout -> 
                logout.permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            AppUser user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}