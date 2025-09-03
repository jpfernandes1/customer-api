package com.neoaplicacoes.customer_api.service;

import com.neoaplicacoes.customer_api.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Customer entity.
 * Provides CRUD operations, filtering, and pagination.
 */
public interface CustomerService {

    // =========================
    // CRUD methods
    // =========================

    /**
     * Create a new customer.
     *
     * @param dto the customer request data
     * @return the created customer response
     */
    CustomerResponseDTO create(CustomerRequestDTO dto);

    /**
     * Update an existing customer by ID.
     *
     * @param id  the ID of the customer to update
     * @param dto the customer request data
     * @return the updated customer response
     */
    CustomerResponseDTO update(Long id, CustomerRequestDTO dto);

    /**
     * Delete a customer by ID.
     *
     * @param id the ID of the customer to delete
     */
    void delete(Long id);

    /**
     * Get a customer by ID.
     *
     * @param id the ID of the customer
     * @return the customer response
     */
    CustomerResponseDTO getById(Long id);

    /**
     * List all customers.
     *
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getAll();

    // =========================
    // Filtering methods
    // =========================

    /**
     * Find customers by name containing text (case-insensitive).
     *
     * @param name text to search in name
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByName(String name);

    /**
     * Find customers by email (exact match, case-insensitive).
     *
     * @param email email to search
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByEmail(String email);

    /**
     * Find customers by CPF (exact match).
     *
     * @param cpf CPF to search
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByCpf(String cpf);

    /**
     * Find customers by city.
     *
     * @param city city to filter
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByCity(String city);

    /**
     * Find customers by state.
     *
     * @param state state to filter
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByState(String state);

    /**
     * Find customers by city and neighborhood.
     *
     * @param city city to filter
     * @param neighborhood neighborhood to filter
     * @return list of customer responses
     */
    List<CustomerResponseDTO> getByCityAndNeighborhood(String city, String neighborhood);

    // Pagination methods

    /**
     * List all customers with pagination.
     *
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getAllPaged(Pageable pageable);

    /**
     * Find customers by name with pagination.
     *
     * @param name     text to search in name
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByNamePaged(String name, Pageable pageable);

    /**
     * Find customers by email with pagination.
     *
     * @param email    email to search
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByEmailPaged(String email, Pageable pageable);

    /**
     * Find customers by CPF with pagination.
     *
     * @param cpf      CPF to search
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByCpfPaged(String cpf, Pageable pageable);

    /**
     * Find customers by city with pagination.
     *
     * @param city     city to filter
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByCityPaged(String city, Pageable pageable);

    /**
     * Find customers by state with pagination.
     *
     * @param state    state to filter
     * @param pageable pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByStatePaged(String state, Pageable pageable);

    /**
     * Find customers by city and neighborhood with pagination.
     *
     * @param city         city to filter
     * @param neighborhood neighborhood to filter
     * @param pageable     pagination information
     * @return paged result of customer responses
     */
    Page<CustomerResponseDTO> getByCityAndNeighborhoodPaged(String city, String neighborhood, Pageable pageable);
}
