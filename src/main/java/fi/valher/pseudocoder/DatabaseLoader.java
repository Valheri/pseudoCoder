package fi.valher.pseudocoder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
// ...other necessary imports (e.g. for AppUser, UserRepository, PasswordEncoder)

import fi.valher.pseudocoder.model.AppUser;
import fi.valher.pseudocoder.repository.UserRepository;

@Component
public class DatabaseLoader {

	@Bean
	public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Check if default user exists; if not, create one.
			if (userRepository.findByUsername("user") == null) {
				AppUser defaultUser = new AppUser();
				defaultUser.setUsername("user");
				defaultUser.setPassword(passwordEncoder.encode("password"));
				defaultUser.setRole("USER");
				userRepository.save(defaultUser);
			}

			// Check if root user exists; if not, create one. hazardous ;)
			if (userRepository.findByUsername("root") == null) {
				AppUser rootUser = new AppUser();
				rootUser.setUsername("root");
				rootUser.setPassword(passwordEncoder.encode("root"));
				rootUser.setRole("ADMIN");
				userRepository.save(rootUser);
			}
		};
	}
}
