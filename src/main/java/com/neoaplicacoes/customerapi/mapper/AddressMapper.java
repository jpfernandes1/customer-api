package com.neoaplicacoes.customerapi.mapper;

import com.neoaplicacoes.customerapi.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customerapi.model.entity.Address;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Address entity and DTOs. Uses MapStruct to automatically
 * generate implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

  /**
   * Converts AddressRequestDTO to Address entity.
   *
   * @param dto the DTO
   * @return Address entity
   */
  Address toEntity(AddressRequestDTO dto);

  /**
   * Converts Address entity to AddressResponseDTO.
   *
   * @param address the entity
   * @return DTO
   */
  AddressResponseDTO toResponse(Address address);

  /**
   * Converts a list of Address entities to a list of AddressResponseDTO.
   *
   * @param addresses list of entities
   * @return list of DTOs
   */
  List<AddressResponseDTO> toResponseList(List<Address> addresses);

  /**
   * Updates an existing Address entity from AddressRequestDTO.
   *
   * @param dto the DTO
   * @param entity the existing entity to update
   */
  void updateFromDto(AddressRequestDTO dto, @MappingTarget Address entity);

  void updateFromDtoPartial(AddressRequestDTO dto, @MappingTarget Address entity);
}
