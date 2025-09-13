package com.neoaplicacoes.customerapi.controller;

import com.neoaplicacoes.customerapi.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customerapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Customers. Provides CRUD operations, filtering, and both paginated
 * and non-paginated endpoints. Includes role-based security and OpenAPI documentation.
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "CRUD operations for customers")
public class CustomerController {

  private final CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  // CRUD BASE

  @Operation(summary = "Create customer", description = "Creates a new customer")
  @PostMapping
  public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto) {
    CustomerResponseDTO created = customerService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(
      summary = "Update customer",
      description = "Updates an existing customer by ID (ADMIN only)")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponseDTO> update(
      @PathVariable Long id, @Valid @RequestBody CustomerRequestDTO dto) {
    CustomerResponseDTO updated = customerService.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Delete customer", description = "Deletes a customer by ID (ADMIN only)")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get customer by ID", description = "Retrieves a customer by ID")
  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id) {
    CustomerResponseDTO customer = customerService.getById(id);
    return ResponseEntity.ok(customer);
  }

  // PAGINATED ENDPOINTS

  @Operation(
      summary = "Get all customers (paginated)",
      description = "Retrieves all customers with pagination")
  @GetMapping
  public ResponseEntity<Page<CustomerResponseDTO>> getAllPaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "name,asc")
          @RequestParam(required = false)
          String sort) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getAllPaged(pageable));
  }

  @Operation(
      summary = "Search customers by name (paginated)",
      description = "Finds customers by name with pagination")
  @GetMapping("/search/by-name")
  public ResponseEntity<Page<CustomerResponseDTO>> getByNamePaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "name,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String name) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getByNamePaged(name, pageable));
  }

  @Operation(
      summary = "Search customers by email (paginated)",
      description = "Finds customers by email with pagination")
  @GetMapping("/search/by-email")
  public ResponseEntity<Page<CustomerResponseDTO>> getByEmailPaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "email,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String email) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getByEmailPaged(email, pageable));
  }

  @Operation(
      summary = "Search customers by CPF (paginated)",
      description = "Finds customers by CPF with pagination")
  @GetMapping("/search/by-cpf")
  public ResponseEntity<Page<CustomerResponseDTO>> getByCpfPaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "name,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String cpf) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getByCpfPaged(cpf, pageable));
  }

  @Operation(
      summary = "Search customers by city (paginated)",
      description = "Finds customers by city with pagination")
  @GetMapping("/search/by-city")
  public ResponseEntity<Page<CustomerResponseDTO>> getByCityPaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String city) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getByCityPaged(city, pageable));
  }

  @Operation(
      summary = "Search customers by state (paginated)",
      description = "Finds customers by state with pagination")
  @GetMapping("/search/by-state")
  public ResponseEntity<Page<CustomerResponseDTO>> getByStatePaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "state,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String state) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(customerService.getByStatePaged(state, pageable));
  }

  @Operation(
      summary = "Search customers by city and neighborhood (paginated)",
      description = "Finds customers by city and neighborhood with pagination")
  @GetMapping("/search/by-city-and-neighborhood")
  public ResponseEntity<Page<CustomerResponseDTO>> getByCityAndNeighborhoodPaged(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int pageNumber,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
          @RequestParam(required = false)
          String sort,
      @RequestParam String city,
      @RequestParam String neighborhood) {

    Pageable pageable = createPageable(pageNumber, size, sort);
    return ResponseEntity.ok(
        customerService.getByCityAndNeighborhoodPaged(city, neighborhood, pageable));
  }

  // Utility method to create Pageable
  private Pageable createPageable(int pageNumber, int size, String sort) {
    if (sort != null && !sort.trim().isEmpty()) {
      String[] sortParams = sort.split(",");
      if (sortParams.length == 2) {
        Sort.Direction direction =
            sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(pageNumber, size, Sort.by(direction, sortParams[0]));
      }
    }
    return PageRequest.of(pageNumber, size);
  }

  // UNPAGINATED ENDPOINTS

  @Operation(
      summary = "Get all customers (unpaginated)",
      description = "Retrieves all customers without pagination")
  @GetMapping("/all")
  public ResponseEntity<List<CustomerResponseDTO>> getAll() {
    return ResponseEntity.ok(customerService.getAll());
  }

  @Operation(
      summary = "Search customers by name (unpaginated)",
      description = "Finds customers by name without pagination")
  @GetMapping("/all/by-name")
  public ResponseEntity<List<CustomerResponseDTO>> getByName(@RequestParam String name) {
    return ResponseEntity.ok(customerService.getByName(name));
  }

  @Operation(
      summary = "Search customers by email (unpaginated)",
      description = "Finds customers by email without pagination")
  @GetMapping("/all/by-email")
  public ResponseEntity<List<CustomerResponseDTO>> getByEmail(@RequestParam String email) {
    return ResponseEntity.ok(customerService.getByEmail(email));
  }

  @Operation(
      summary = "Search customers by CPF (unpaginated)",
      description = "Finds customers by CPF without pagination")
  @GetMapping("/all/by-cpf")
  public ResponseEntity<List<CustomerResponseDTO>> getByCpf(@RequestParam String cpf) {
    return ResponseEntity.ok(customerService.getByCpf(cpf));
  }

  @Operation(
      summary = "Search customers by city (unpaginated)",
      description = "Finds customers by city without pagination")
  @GetMapping("/all/by-city")
  public ResponseEntity<List<CustomerResponseDTO>> getByCity(@RequestParam String city) {
    return ResponseEntity.ok(customerService.getByCity(city));
  }

  @Operation(
      summary = "Search customers by state (unpaginated)",
      description = "Finds customers by state without pagination")
  @GetMapping("/all/by-state")
  public ResponseEntity<List<CustomerResponseDTO>> getByState(@RequestParam String state) {
    return ResponseEntity.ok(customerService.getByState(state));
  }

  @Operation(
      summary = "Search customers by city and neighborhood (unpaginated)",
      description = "Finds customers by city and neighborhood without pagination")
  @GetMapping("/all/by-city-and-neighborhood")
  public ResponseEntity<List<CustomerResponseDTO>> getByCityAndNeighborhood(
      @RequestParam String city, @RequestParam String neighborhood) {
    return ResponseEntity.ok(customerService.getByCityAndNeighborhood(city, neighborhood));
  }
}
