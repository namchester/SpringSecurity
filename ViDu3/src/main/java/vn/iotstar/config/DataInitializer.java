package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_USERNAME:admin}") String adminUsername,
            @Value("${ADMIN_EMAIL:admin@ute.edu.vn}") String adminEmail,
            @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {

        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name("ROLE_USER").build()));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name("ROLE_ADMIN").build()));

            String normalizedAdminUsername = adminUsername.trim();
            String normalizedAdminEmail = adminEmail.trim().toLowerCase();

            if (!userRepository.existsByUsernameIgnoreCase(normalizedAdminUsername)
                    && !userRepository.existsByEmailIgnoreCase(normalizedAdminEmail)) {
                userRepository.save(User.builder()
                        .username(normalizedAdminUsername)
                        .email(normalizedAdminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .fullName("System Administrator")
                        .enabled(true)
                        .role(adminRole)
                        .build());
            }

            if (!userRepository.existsByUsernameIgnoreCase("user01")
                    && !userRepository.existsByEmailIgnoreCase("user01@gmail.com")) {
                userRepository.save(User.builder()
                        .username("user01")
                        .email("user01@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Sinh viên UTE")
                        .enabled(true)
                        .role(userRole)
                        .build());
            }
        };
    }
}
