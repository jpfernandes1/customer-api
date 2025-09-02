package com.neoaplicacoes.customer_api.repository;

import com.neoaplicacoes.customer_api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for {@link Customer} entity.
 * Provides CRUD operations, pagination, and common filtering queries.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Basic lookup methods

    /**
     * Find a customer by email.
     *
     * @param email the email of the customer
     * @return Optional containing the customer if found
     */
    List<Customer> findByEmailIgnoreCase(String email);

    /**
     * Find a customer by CPF.
     *
     * @param cpf the CPF of the customer
     * @return Optional containing the customer if found
     */
    List<Customer> findByCpf(String cpf);

    // Filtering methods (list)

    /**
     * Find customers by name (case-insensitive).
     *
     * @param name the name to search for
     * @return list of matching customers
     */
    List<Customer> findByNameContainingIgnoreCase(String name);

    /**
     * Find customers by email (case-insensitive).
     *
     * @param email the email to search for
     * @return list of matching customers
     */
    List<Customer> findByEmailContainingIgnoreCase(String email);

    /**
     * Find customers by CPF.
     *
     * @param cpf the CPF to search for
     * @return list of matching customers
     */
    List<Customer> findByCpfContaining(String cpf);

    // Filtering methods (paged)

    /**
     * Find customers by name (case-insensitive) with pagination.
     *
     * @param name the name to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Find customers by email (case-insensitive) with pagination.
     *
     * @param email the email to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    /**
     * Find customers by CPF with pagination.
     *
     * @param cpf the CPF to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByCpfContaining(String cpf, Pageable pageable);

    // Convenience methods

    /**
     * Find customers by name or email containing the search term (case-insensitive) with pagination.
     *
     * @param name the name to search for
     * @param email the email to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    /**
     * Find customers by city in the address (case-insensitive).
     *
     * @param city the city to search for
     * @return list of matching customers
     */
    List<Customer> findByAddressCityIgnoreCase(String city);

    /**
     * Find customers by city in the address (case-insensitive) with pagination.
     *
     * @param city the city to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByAddressCityIgnoreCase(String city, Pageable pageable);

    /**
     * Find customers by state (case-insensitive).
     *
     * @param state the state to search for
     * @return list of matching customers
     */
    List<Customer> findByStateIgnoreCase(String state);

    /**
     * Find customers by state (case-insensitive) with pagination.
     *
     * @param state the state to search for
     * @param pageable page request information
     * @return paged result of customers
     */
    Page<Customer> findByStateIgnoreCase(String state, Pageable pageable);

    /**
     * Find customers by city and neighborhood (case-insensitive).
     *
     * @param city the city to search for
     * @return list of matching customers
     */
    List<Customer> findByCityIgnoreCaseAndNeighborhoodIgnoreCase(String city, String neighborhood);

    /**
     * Find customers by city and neighborhood (case-insensitive) with pagination.
     *
     * @param city the city to search for
     * @param neighborhood the neighborhood to search for
     * @return paged result of customers
     */
    Page<Customer> findByCityIgnoreCaseAndNeighborhoodIgnoreCase(String city, String neighborhood, Pageable pageable);
}
