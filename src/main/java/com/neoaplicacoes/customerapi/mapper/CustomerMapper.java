package com.neoaplicacoes.customerapi.mapper;

import com.neoaplicacoes.customerapi.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customerapi.model.entity.Customer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting between Customer entity and DTOs. Uses MapStruct to automatically
 * generate implementation at compile time. Calculates age when converting to CustomerResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

  /**
   * Converts Customer entity to CustomerResponseDTO. Calculates the age from birthDate.
   *
   * @param customer the entity
   * @return DTO with age calculated
   */
  @Mapping(target = "age", expression = "java(calculateAge(customer.getBirthDate()))")
  CustomerResponseDTO toDto(Customer customer);

  /**
   * Converts a list of Customer entities to a list of CustomerResponseDTO.
   *
   * @param customers list of entities
   * @return list of DTOs with age calculated
   */
  List<CustomerResponseDTO> toDtoList(List<Customer> customers);

  /**
   * Converts CustomerRequestDTO to Customer entity.
   *
   * @param dto the DTO
   * @return Customer entity
   */
  Customer toEntity(CustomerRequestDTO dto);

  /**
   * Updates an existing Customer entity from CustomerRequestDTO.
   *
   * @param dto the DTO
   * @param entity the existing entity to update
   */
  void updateEntityFromDto(CustomerRequestDTO dto, @MappingTarget Customer entity);

  /**
   * Helper method to calculate age from birthDate.
   *
   * @param birthDate the birth date
   * @return age in years, or null if birthDate is null
   */
  default Integer calculateAge(LocalDate birthDate) {
    if (birthDate == null) {
      return null;
    }
    return Period.between(birthDate, LocalDate.now()).getYears();
  }
}
