package com.neoaplicacoes.customerapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.neoaplicacoes.customerapi.model.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    user1 = new User();
    user1.setEmail("admin@example.com");
    user1.setPassword("password123");
    user1.setRole("ROLE_ADMIN");
    user1.setActive(true);

    user2 = new User();
    user2.setEmail("user@example.com");
    user2.setPassword("password123");
    user2.setRole("ROLE_USER");
    user2.setActive(true);

    user3 = new User();
    user3.setEmail("inactive@example.com");
    user3.setPassword("password123");
    user3.setRole("ROLE_USER");
    user3.setActive(false);

    userRepository.saveAll(List.of(user1, user2, user3));
  }

  // Basic lookup tests

  @Test
  @DisplayName("findByEmailIgnoreCase returns correct user")
  void testFindByEmailIgnoreCase() {
    List<User> users = userRepository.findByEmailIgnoreCase("ADMIN@EXAMPLE.COM");
    assertThat(users).hasSize(1).contains(user1);
  }

  @Test
  @DisplayName("findByRoleIgnoreCase returns correct users")
  void testFindByRoleIgnoreCase() {
    List<User> users = userRepository.findByRoleIgnoreCase("role_user");
    assertThat(users).hasSize(2).contains(user2, user3);
  }

  @Test
  @DisplayName("findByActive returns correct users")
  void testFindByActive() {
    List<User> activeUsers = userRepository.findByActive(true);
    List<User> inactiveUsers = userRepository.findByActive(false);

    assertThat(activeUsers).hasSize(2).contains(user1, user2);
    assertThat(inactiveUsers).hasSize(1).contains(user3);
  }

  // Pagination tests

  @Test
  @DisplayName("findByEmailContainingIgnoreCase returns paged results")
  void testFindByEmailContainingIgnoreCase() {
    Page<User> page =
        userRepository.findByEmailContainingIgnoreCase("example", PageRequest.of(0, 2));
    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getContent()).contains(user1, user2);
  }

  @Test
  @DisplayName("findByRoleIgnoreCase returns paged results")
  void testFindByRoleIgnoreCaseWithPagination() {
    Page<User> page = userRepository.findByRoleIgnoreCase("ROLE_USER", PageRequest.of(0, 1));
    assertThat(page.getTotalElements()).isEqualTo(2);
    assertThat(page.getContent()).containsAnyOf(user2, user3);
  }

  @Test
  @DisplayName("findByActive returns paged results")
  void testFindByActiveWithPagination() {
    Page<User> page = userRepository.findByActive(true, PageRequest.of(0, 1));
    assertThat(page.getTotalElements()).isEqualTo(2);
    assertThat(page.getContent()).containsAnyOf(user1, user2);
  }
}
