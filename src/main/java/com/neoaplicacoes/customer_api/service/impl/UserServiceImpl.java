package com.neoaplicacoes.customer_api.service.impl;

import com.neoaplicacoes.customer_api.model.dto.request.UserAdminRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.model.entity.User;
import com.neoaplicacoes.customer_api.mapper.UserMapper;
import com.neoaplicacoes.customer_api.repository.UserRepository;
import com.neoaplicacoes.customer_api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link UserService}.
 * Provides CRUD operations, filtering, pagination, and self-registration.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // CRUD methods

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        // Encodes password
        UserRequestDTO dtoWithEncodedPassword = new UserRequestDTO(
                dto.email(),
                passwordEncoder.encode(dto.password())

        );

        User user = userMapper.toEntity(dtoWithEncodedPassword);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userMapper.updateEntityFromDto(dto, existing);
        User updated = userRepository.save(existing);
        return userMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userRepository.delete(existing);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public List<UserResponseDTO> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    // Filtering methods

    @Override
    public List<UserResponseDTO> getByEmail(String email) {
        List<User> users = userRepository.findByEmailIgnoreCase(email);
        return userMapper.toDtoList(users);
    }

    @Override
    public List<UserResponseDTO> getByRole(String role) {
        List<User> users = userRepository.findByRoleIgnoreCase(role);
        return userMapper.toDtoList(users);
    }

    @Override
    public List<UserResponseDTO> getByActive(Boolean active) {
        List<User> users = userRepository.findByActive(active);
        return userMapper.toDtoList(users);
    }

    // Pagination methods

    @Override
    public Page<UserResponseDTO> getAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> getByEmailPaged(String email, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCase(email, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> getByRolePaged(String role, Pageable pageable) {
        return userRepository.findByRoleIgnoreCase(role, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> getByActivePaged(Boolean active, Pageable pageable) {
        return userRepository.findByActive(active, pageable)
                .map(userMapper::toDto);
    }

    // Self-registration

    /**
     * Registers a new user via self-registration.
     * Forces role to USER and sets active to true.
     */
    public UserResponseDTO register(UserRequestDTO dto) {
        UserRequestDTO registrationDto = new UserRequestDTO(
                dto.email(),
                passwordEncoder.encode(dto.password())
        );

        User user = userMapper.toEntity(registrationDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDTO updateAdmin(Long id, UserAdminRequestDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        // Atualização parcial dos campos administrativos
        updateAdminFields(dto, existing);

        User updated = userRepository.save(existing);
        return userMapper.toDto(updated);
    }

    private void updateAdminFields(UserAdminRequestDTO dto, User entity) {
        if (dto.role() != null) {
            entity.setRole(dto.role());
        }
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }
    }

}
