package fi.valher.pseudocoder;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

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
			.requiresChannel(channel -> channel.anyRequest().requiresSecure()) // In production, HTTPS is enforced; adjust for local development if needed
			.cors().and() // enable CORS support
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(
					antMatcher("/css/**"),
					antMatcher("/login"),
					antMatcher("/api/login"))
				.permitAll()
				// Permit OPTIONS requests
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/**").permitAll() // optionally permit API endpoints
				.anyRequest().authenticated()) // Protect all other endpoints including /pseudoCodes
			.formLogin(formlogin -> formlogin
				.loginPage("/login")
				.defaultSuccessUrl("/pseudoCodes", true)
				.permitAll())
			.logout(logout -> logout.permitAll());
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




