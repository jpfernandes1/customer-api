package com.neoaplicacoes.customer_api.controller;

import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Address entity.
 * Provides CRUD operations with role-based security and OpenAPI documentation.
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Create a new address")
    @ApiResponse(responseCode = "201", description = "Address created successfully")
    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO created = addressService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update an existing address by ID")
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO dto
    ) {
        AddressResponseDTO updated = addressService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete an address by ID (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Address deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get an address by ID")
    @ApiResponse(responseCode = "200", description = "Address retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Long id) {
        AddressResponseDTO dto = addressService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all addresses")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAll() {
        List<AddressResponseDTO> list = addressService.getAll();
        return ResponseEntity.ok(list);
    }
}
