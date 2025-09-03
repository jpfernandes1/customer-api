package com.neoaplicacoes.customer_api.controller;

import com.neoaplicacoes.customer_api.model.dto.request.UserAdminRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "CRUD operations for users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Public self-registration
    @Operation(summary = "Register self", description = "Allows a new user to create their own account")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Creation and management by Admin
    @Operation(summary = "Create user (ADMIN only)", description = "Admin can create new users")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createByAdmin(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update user", description = "Updates an existing user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto
    ) {
        UserResponseDTO updated = userService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Partial update of user admin fields", description = "Update role or active status (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/admin")
    public ResponseEntity<UserResponseDTO> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UserAdminRequestDTO dto
    ) {
        UserResponseDTO updatedUser = userService.updateAdmin(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        UserResponseDTO dto = userService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "List users", description = "Returns all users (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<UserResponseDTO> list = userService.getAll();
        return ResponseEntity.ok(list);
    }

    // UNPAGINATED ENDPOINTS
    @Operation(summary = "Find users by email (unpaginated)", description = "Exact match, case-insensitive")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/by-email")
    public ResponseEntity<List<UserResponseDTO>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @Operation(summary = "Find users by role (unpaginated)", description = "Case-insensitive")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/by-role")
    public ResponseEntity<List<UserResponseDTO>> getByRole(@RequestParam String role) {
        return ResponseEntity.ok(userService.getByRole(role));
    }

    @Operation(summary = "Find users by active status (unpaginated)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/by-active")
    public ResponseEntity<List<UserResponseDTO>> getByActive(@RequestParam Boolean active) {
        return ResponseEntity.ok(userService.getByActive(active));
    }

    // Paginated Endpoints
    @Operation(summary = "Find users by email (paginated)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/by-email")
    public ResponseEntity<Page<UserResponseDTO>> getByEmailPaged(
            @RequestParam String email,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getByEmailPaged(email, pageable));
    }

    @Operation(summary = "Find users by role (paginated)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/by-role")
    public ResponseEntity<Page<UserResponseDTO>> getByRolePaged(
            @RequestParam String role,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getByRolePaged(role, pageable));
    }

    @Operation(summary = "Find users by active status (paginated)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/by-active")
    public ResponseEntity<Page<UserResponseDTO>> getByActivePaged(
            @RequestParam Boolean active,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getByActivePaged(active, pageable));
    }

    @Operation(summary = "List users (paginated)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paged")
    public ResponseEntity<Page<UserResponseDTO>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllPaged(pageable));
    }
}
