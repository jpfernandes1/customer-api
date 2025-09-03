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
     * List all addresses.
     *
     * @return list of address responses
     */
    List<AddressResponseDTO> getAll();

    // =========================
    // Filters without pagination
    // =========================

    /**
     * Find addresses by city (case-insensitive).
     *
     * @param city the city to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByCity(String city);

    /**
     * Find addresses by state (case-insensitive).
     *
     * @param state the state to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByState(String state);

    /**
     * Find addresses by neighborhood (case-insensitive).
     *
     * @param neighborhood the neighborhood to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByNeighborhood(String neighborhood);

    /**
     * Find addresses by city and neighborhood (case-insensitive).
     *
     * @param city         the city to filter
     * @param neighborhood the neighborhood to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByCityAndNeighborhood(String city, String neighborhood);

    /**
     * Find addresses by postal code.
     *
     * @param cep the postal code
     * @return list of address responses
     */
    List<AddressResponseDTO> getByCep(String cep);

    /**
     * Find addresses by postal code and state (case-insensitive).
     *
     * @param cep   the postal code
     * @param state the state to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByCepAndState(String cep, String state);

    /**
     * Find addresses containing street (case-insensitive).
     *
     * @param street the street to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByStreet(String street);

    /**
     * Find addresses by city and containing street (case-insensitive).
     *
     * @param city   the city to filter
     * @param street the street to filter
     * @return list of address responses
     */
    List<AddressResponseDTO> getByCityAndStreet(String city, String street);

    // =========================
    // Filters with pagination
    // =========================

    /**
     * Find addresses by city (case-insensitive) with pagination.
     *
     * @param city     the city to filter
     * @param pageable pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByCityPaged(String city, Pageable pageable);

    /**
     * Find addresses by state (case-insensitive) with pagination.
     *
     * @param state    the state to filter
     * @param pageable pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByStatePaged(String state, Pageable pageable);

    /**
     * Find addresses by neighborhood (case-insensitive) with pagination.
     *
     * @param neighborhood the neighborhood to filter
     * @param pageable     pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByNeighborhoodPaged(String neighborhood, Pageable pageable);

    /**
     * Find addresses by city and neighborhood (case-insensitive) with pagination.
     *
     * @param city         the city to filter
     * @param neighborhood the neighborhood to filter
     * @param pageable     pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByCityAndNeighborhoodPaged(String city, String neighborhood, Pageable pageable);

    /**
     * Find addresses containing street (case-insensitive) with pagination.
     *
     * @param street   the street to filter
     * @param pageable pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByStreetPaged(String street, Pageable pageable);

    /**
     * Find addresses by city and containing street (case-insensitive) with pagination.
     *
     * @param city     the city to filter
     * @param street   the street to filter
     * @param pageable pagination information
     * @return paged result of addresses
     */
    Page<AddressResponseDTO> getByCityAndStreetPaged(String city, String street, Pageable pageable);
}
