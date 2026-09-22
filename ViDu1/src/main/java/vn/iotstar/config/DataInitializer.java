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
    CommandLineRunner initData(RoleRepository roles,
                               UserRepository users,
                               PasswordEncoder encoder,
                               @Value("${ADMIN_EMAIL:admin@ute.edu.vn}") String adminEmail,
                               @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {
        return args -> {
            Role userRole = roles.findByNameIgnoreCase("USER").orElseGet(() -> roles.save(new Role("USER")));
            Role adminRole = roles.findByNameIgnoreCase("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));

            if (!users.existsByEmailIgnoreCase(adminEmail)) {
                users.save(User.builder()
                        .email(adminEmail.toLowerCase())
                        .fullName("System Administrator")
                        .password(encoder.encode(adminPassword))
                        .role(adminRole)
                        .enabled(true)
                        .build());
            }

            if (!users.existsByEmailIgnoreCase("user@ute.edu.vn")) {
                users.save(User.builder()
                        .email("user@ute.edu.vn")
                        .fullName("Sinh viên UTE")
                        .password(encoder.encode("123456"))
                        .role(userRole)
                        .enabled(true)
                        .build());
            }
        };
    }
}
