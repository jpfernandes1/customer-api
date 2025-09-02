package com.neoaplicacoes.customer_api.mapper;

import com.neoaplicacoes.customer_api.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between User entity and DTOs.
 * Uses MapStruct to automatically generate implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts UserRequestDTO to User entity.
     *
     * @param dto the DTO
     * @return User entity
     */
    User toEntity(UserRequestDTO dto);

    /**
     * Converts User entity to UserResponseDTO.
     *
     * @param user the entity
     * @return DTO
     */
    UserResponseDTO toDto(User user);

    /**
     * Converts a list of User entities to a list of UserResponseDTO.
     *
     * @param users list of entities
     * @return list of DTOs
     */
    List<UserResponseDTO> toDtoList(List<User> users);

    /**
     * Updates an existing User entity from UserRequestDTO.
     *
     * @param dto the DTO
     * @param entity the existing entity to update
     */
    void updateEntityFromDto(UserRequestDTO dto, @MappingTarget User entity);
}
