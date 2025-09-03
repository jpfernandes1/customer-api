package com.neoaplicacoes.customer_api.controller;

import com.neoaplicacoes.customer_api.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customer_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Customers.
 * Provides CRUD operations with role-based security and OpenAPI documentation.
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

    @Operation(summary = "Create customer", description = "Creates a new customer")
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO created = customerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO dto
    ) {
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

}
