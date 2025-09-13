package com.neoaplicacoes.customerapi.repository;

import com.neoaplicacoes.customerapi.model.entity.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Address} entity. Provides CRUD operations, pagination, and common
 * filtering queries.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  // Basic lookup methods

  /**
   * Find an address by its postal code.
   *
   * @param cep the postal code
   * @return Optional containing the list if found
   */
  Optional<Address> findByCep(String cep);

  /**
   * Find an address by postal code, street, and number.
   *
   * @param cep the postal code
   * @param street the street name
   * @param number the number
   * @return Optional containing the address if found
   */
  Optional<Address> findByCepAndStreetAndNumber(String cep, String street, String number);

  // Common filters

  /**
   * Find addresses by city (case-insensitive).
   *
   * @param city the city name
   * @return list of matching addresses
   */
  List<Address> findByCityIgnoreCase(String city);

  Page<Address> findByCityIgnoreCase(String city, Pageable pageable);

  List<Address> findByStateIgnoreCase(String state);

  Page<Address> findByStateIgnoreCase(String state, Pageable pageable);

  List<Address> findByNeighborhoodIgnoreCase(String neighborhood);

  Page<Address> findByNeighborhoodIgnoreCase(String neighborhood, Pageable pageable);

  List<Address> findByCityIgnoreCaseAndNeighborhoodIgnoreCase(String city, String neighborhood);

  Page<Address> findByCityIgnoreCaseAndNeighborhoodIgnoreCase(
      String city, String neighborhood, Pageable pageable);

  List<Address> findByCepAndStateIgnoreCase(String cep, String state);

  // Convenience filters

  List<Address> findByStreetContainingIgnoreCase(String street);

  Page<Address> findByStreetContainingIgnoreCase(String street, Pageable pageable);

  List<Address> findByCityIgnoreCaseAndStreetContainingIgnoreCase(String city, String street);

  Page<Address> findByCityIgnoreCaseAndStreetContainingIgnoreCase(
      String city, String street, Pageable pageable);
}
