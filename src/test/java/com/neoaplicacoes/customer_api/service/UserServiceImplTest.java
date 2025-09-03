package com.neoaplicacoes.customer_api.service.impl;

import com.neoaplicacoes.customer_api.mapper.UserMapper;
import com.neoaplicacoes.customer_api.model.dto.request.UserAdminRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.model.entity.User;
import com.neoaplicacoes.customer_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void create_shouldEncodePasswordAndSaveUser() {
        UserRequestDTO dto = new UserRequestDTO("email@test.com", "plainpass");
        User entity = new User();
        User savedEntity = new User();
        UserResponseDTO responseDto = new UserResponseDTO(1L, "email@test.com", "ROLE_USER", true, null, null);

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedpass");
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(savedEntity);
        when(userMapper.toDto(savedEntity)).thenReturn(responseDto);

        UserResponseDTO result = userService.create(dto);

        assertThat(result).isEqualTo(responseDto);
        verify(passwordEncoder).encode("plainpass");
        verify(userRepository).save(entity);
        verify(userMapper).toEntity(any(UserRequestDTO.class));
        verify(userMapper).toDto(savedEntity);
    }

    @Test
    void update_existingUser_shouldUpdateAndReturnDto() {
        Long id = 1L;
        UserRequestDTO dto = new UserRequestDTO("new@test.com", "newpass");
        User existing = new User();
        User updated = new User();
        UserResponseDTO responseDto = new UserResponseDTO(1L, "new@test.com", "ROLE_USER", true, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(userMapper).updateEntityFromDto(dto, existing);
        when(userRepository.save(existing)).thenReturn(updated);
        when(userMapper.toDto(updated)).thenReturn(responseDto);

        UserResponseDTO result = userService.update(id, dto);

        assertThat(result).isEqualTo(responseDto);
        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
        verify(userMapper).updateEntityFromDto(dto, existing);
    }

    @Test
    void update_nonExistingUser_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1L, new UserRequestDTO("a", "b")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with id 1");
    }

    @Test
    void delete_existingUser_shouldCallRepository() {
        User existing = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        userService.delete(1L);

        verify(userRepository).delete(existing);
    }

    @Test
    void delete_nonExistingUser_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with id 1");
    }

    @Test
    void getById_existingUser_shouldReturnDto() {
        User entity = new User();
        UserResponseDTO dto = new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(dto);

        assertThat(userService.getById(1L)).isEqualTo(dto);
    }

    @Test
    void getById_nonExistingUser_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getAll_shouldReturnDtoList() {
        List<User> users = List.of(new User());
        List<UserResponseDTO> dtos = List.of(new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null));
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        assertThat(userService.getAll()).isEqualTo(dtos);
    }

    @Test
    void getByEmail_shouldReturnDtoList() {
        List<User> users = List.of(new User());
        List<UserResponseDTO> dtos = List.of(new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null));
        when(userRepository.findByEmailIgnoreCase("x")).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        assertThat(userService.getByEmail("x")).isEqualTo(dtos);
    }

    @Test
    void getByRole_shouldReturnDtoList() {
        List<User> users = List.of(new User());
        List<UserResponseDTO> dtos = List.of(new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null));
        when(userRepository.findByRoleIgnoreCase("ROLE_USER")).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        assertThat(userService.getByRole("ROLE_USER")).isEqualTo(dtos);
    }

    @Test
    void getByActive_shouldReturnDtoList() {
        List<User> users = List.of(new User());
        List<UserResponseDTO> dtos = List.of(new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null));
        when(userRepository.findByActive(true)).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        assertThat(userService.getByActive(true)).isEqualTo(dtos);
    }

    @Test
    void getAllPaged_shouldReturnPageDto() {
        User user = new User();
        Page<User> page = new PageImpl<>(List.of(user));
        Page<UserResponseDTO> pageDto = new PageImpl<>(List.of(new UserResponseDTO(1L, "e", "ROLE_USER", true, null, null)));

        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(userMapper.toDto(user)).thenReturn(pageDto.getContent().get(0));

        assertThat(userService.getAllPaged(PageRequest.of(0, 10)).getContent()).isEqualTo(pageDto.getContent());
    }

    @Test
    void register_shouldEncodePasswordAndSave() {
        UserRequestDTO dto = new UserRequestDTO("email@test.com", "plainpass");
        User userEntity = new User();
        User savedEntity = new User();
        UserResponseDTO responseDto = new UserResponseDTO(1L, "email@test.com", "ROLE_USER", true, null, null);

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedpass");
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userMapper.toDto(savedEntity)).thenReturn(responseDto);

        UserResponseDTO result = userService.register(dto);

        assertThat(result).isEqualTo(responseDto);
        verify(passwordEncoder).encode("plainpass");
    }

    @Test
    void updateAdmin_shouldUpdateFields() {
        Long id = 1L;
        UserAdminRequestDTO dto = new UserAdminRequestDTO(null, false);
        User existing = new User();
        User updated = new User();
        UserResponseDTO responseDto = new UserResponseDTO(1L, "e", "ROLE_USER", false, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(updated);
        when(userMapper.toDto(updated)).thenReturn(responseDto);

        UserResponseDTO result = userService.updateAdmin(id, dto);

        assertThat(result).isEqualTo(responseDto);
        assertThat(existing.getActive()).isFalse();
    }

    @Test
    void updateAdmin_nonExistingUser_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserAdminRequestDTO dto = new UserAdminRequestDTO(null, true);

        assertThatThrownBy(() -> userService.updateAdmin(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
