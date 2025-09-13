package com.neoaplicacoes.customerapi.repository;

import com.neoaplicacoes.customerapi.model.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link User} entity. Provides CRUD operations, filtering, and
 * pagination.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  // =========================
  // Basic lookup methods
  // =========================

  /**
   * Find users by email (case-insensitive).
   *
   * @param email the email to search for
   * @return list of matching users
   */
  List<User> findByEmailIgnoreCase(String email);

  /**
   * Find users by role (case-insensitive).
   *
   * @param role the role to search for
   * @return list of matching users
   */
  List<User> findByRoleIgnoreCase(String role);

  /**
   * Find users by active status.
   *
   * @param active the active status to filter
   * @return list of matching users
   */
  List<User> findByActive(Boolean active);

  // =========================
  // Filtering methods with pagination
  // =========================

  /**
   * Find users by email containing text (case-insensitive) with pagination.
   *
   * @param email the email to search for
   * @param pageable page request information
   * @return paged result of users
   */
  Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

  /**
   * Find users by role (case-insensitive) with pagination.
   *
   * @param role the role to search for
   * @param pageable page request information
   * @return paged result of users
   */
  Page<User> findByRoleIgnoreCase(String role, Pageable pageable);

  /**
   * Find users by active status with pagination.
   *
   * @param active the active status to filter
   * @param pageable page request information
   * @return paged result of users
   */
  Page<User> findByActive(Boolean active, Pageable pageable);
}
