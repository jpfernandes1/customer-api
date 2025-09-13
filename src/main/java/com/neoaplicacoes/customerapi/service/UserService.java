package com.neoaplicacoes.customerapi.service;

import com.neoaplicacoes.customerapi.model.dto.request.UserAdminRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.UserResponseDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service interface for User entity. Provides CRUD operations, filtering, and pagination. */
public interface UserService {

  // =========================
  // CRUD methods
  // =========================

  /**
   * Create a new user.
   *
   * @param dto the user request data
   * @return the created user response
   */
  UserResponseDTO create(UserRequestDTO dto);

  /**
   * Register a new user (self-registration). Forces the role to USER regardless of input.
   *
   * @param dto the user request data
   * @return the created user response
   */
  UserResponseDTO register(UserRequestDTO dto);

  /**
   * Update an existing user by ID.
   *
   * @param id the ID of the user to update
   * @param dto the user request data
   * @return the updated user response
   */
  UserResponseDTO update(Long id, UserRequestDTO dto);

  /**
   * Delete a user by ID.
   *
   * @param id the ID of the user to delete
   */
  void delete(Long id);

  /**
   * Get a user by ID.
   *
   * @param id the ID of the user
   * @return the user response
   */
  UserResponseDTO getById(Long id);

  /**
   * List all users.
   *
   * @return list of user responses
   */
  List<UserResponseDTO> getAll();

  // =========================
  // Filtering methods
  // =========================

  /**
   * Find users by email (exact match, case-insensitive).
   *
   * @param email email to search
   * @return list of user responses
   */
  List<UserResponseDTO> getByEmail(String email);

  /**
   * Find users by role (case-insensitive).
   *
   * @param role role to filter
   * @return list of user responses
   */
  List<UserResponseDTO> getByRole(String role);

  /**
   * Find users by active status.
   *
   * @param active active status to filter
   * @return list of user responses
   */
  List<UserResponseDTO> getByActive(Boolean active);

  // =========================
  // Pagination methods
  // =========================

  /**
   * List all users with pagination.
   *
   * @param pageable pagination information
   * @return paged result of user responses
   */
  Page<UserResponseDTO> getAllPaged(Pageable pageable);

  /**
   * Find users by email with pagination.
   *
   * @param email email to search
   * @param pageable pagination information
   * @return paged result of user responses
   */
  Page<UserResponseDTO> getByEmailPaged(String email, Pageable pageable);

  /**
   * Find users by role with pagination.
   *
   * @param role role to filter
   * @param pageable pagination information
   * @return paged result of user responses
   */
  Page<UserResponseDTO> getByRolePaged(String role, Pageable pageable);

  /**
   * Find users by active status with pagination.
   *
   * @param active active status to filter
   * @param pageable pagination information
   * @return paged result of user responses
   */
  Page<UserResponseDTO> getByActivePaged(Boolean active, Pageable pageable);

  UserResponseDTO updateAdmin(Long id, UserAdminRequestDTO dto);
}
