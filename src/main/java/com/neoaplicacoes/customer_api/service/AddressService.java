package com.neoaplicacoes.customer_api.service;

import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Address entity.
 * Provides CRUD operations, filtering, and pagination.
 */
public interface AddressService {

    /**
     * Create a new address.
     *
     * @param dto the address request data
     * @return the created address response
     */
    AddressResponseDTO create(AddressRequestDTO dto);

    /**
     * Update an existing address by ID.
     *
     * @param id  the ID of the address to update
     * @param dto the address request data
     * @return the updated address response
     */
    AddressResponseDTO update(Long id, AddressRequestDTO dto);

    /**
     * Delete an address by ID.
     *
     * @param id the ID of the address to delete
     */
    void delete(Long id);

    /**
     * Get an address by ID.
     *
     * @param id the ID of the address
     * @return the address response
     */
    AddressResponseDTO getById(Long id);

    /**
     * List all addresses without pagination.
     *
     * @return list of address responses
     */
    List<AddressResponseDTO> getAll();

    /**
     * List all addresses with pagination.
     *
     * @param pageable pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getAllPaged(Pageable pageable);

    // Filters without pagination

    List<AddressResponseDTO> getByCity(String city);
    List<AddressResponseDTO> getByState(String state);
    List<AddressResponseDTO> getByNeighborhood(String neighborhood);
    List<AddressResponseDTO> getByCityAndNeighborhood(String city, String neighborhood);
    List<AddressResponseDTO> getByCep(String cep);
    List<AddressResponseDTO> getByCepAndState(String cep, String state);
    List<AddressResponseDTO> getByStreet(String street);
    List<AddressResponseDTO> getByCityAndStreet(String city, String street);


    // Filters with pagination

    Page<AddressResponseDTO> getByCityPaged(String city, Pageable pageable);
    Page<AddressResponseDTO> getByStatePaged(String state, Pageable pageable);
    Page<AddressResponseDTO> getByNeighborhoodPaged(String neighborhood, Pageable pageable);
    Page<AddressResponseDTO> getByCityAndNeighborhoodPaged(String city, String neighborhood, Pageable pageable);
    Page<AddressResponseDTO> getByStreetPaged(String street, Pageable pageable);
    Page<AddressResponseDTO> getByCityAndStreetPaged(String city, String street, Pageable pageable);
}
