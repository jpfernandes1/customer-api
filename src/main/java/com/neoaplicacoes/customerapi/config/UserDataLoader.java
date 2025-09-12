package com.neoaplicacoes.customerapi.config;

import com.neoaplicacoes.customerapi.model.entity.User;
import com.neoaplicacoes.customerapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.count() == 0) {
      createAdminUser();
      createCommonUser();
      System.out.println("✅ Initial users created successfully!");
    }
  }

  private void createAdminUser() {
    if (userRepository.findByEmailIgnoreCase("admin@email.com").isEmpty()) {
      User admin = new User();
      admin.setEmail("admin@email.com");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setRole("ROLE_ADMIN");
      admin.setActive(true);
      userRepository.save(admin);
      System.out.println("👨‍💼 Admin user created: admin@email.com / admin123");
    }
  }

  private void createCommonUser() {
    if (userRepository.findByEmailIgnoreCase("user@email.com").isEmpty()) {
      User user = new User();
      user.setEmail("user@email.com");
      user.setPassword(passwordEncoder.encode("user123"));
      user.setRole("ROLE_USER");
      user.setActive(true);
      userRepository.save(user);
      System.out.println("👤 Common user created: user@email.com / user123");
    }
  }
}
