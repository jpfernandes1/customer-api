package com.neoaplicacoes.customer_api.controller;

import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Addresses.
 * Provides CRUD operations, filtering, pagination, and role-based security.
 */
@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "CRUD operations for addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // CRUD


    @Operation(summary = "Create address", description = "Creates a new address")
    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO created = addressService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update address", description = "Updates an existing address by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO updated = addressService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete address", description = "Deletes an address by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get address by ID", description = "Retrieves an address by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Long id) {
        AddressResponseDTO dto = addressService.getById(id);
        return ResponseEntity.ok(dto);
    }

    // UNPAGINATED SEARCH


    @Operation(summary = "Get all addresses (unpaginated)", description = "Retrieves all addresses without pagination")
    @GetMapping("/all")
    public ResponseEntity<List<AddressResponseDTO>> getAll() {
        return ResponseEntity.ok(addressService.getAll());
    }

    @Operation(summary = "Search addresses by city (unpaginated)", description = "Filter addresses by city without pagination")
    @GetMapping("/all/by-city")
    public ResponseEntity<List<AddressResponseDTO>> getByCity(@RequestParam String city) {
        return ResponseEntity.ok(addressService.getByCity(city));
    }

    @Operation(summary = "Search addresses by state (unpaginated)", description = "Filter addresses by state without pagination")
    @GetMapping("/all/by-state")
    public ResponseEntity<List<AddressResponseDTO>> getByState(@RequestParam String state) {
        return ResponseEntity.ok(addressService.getByState(state));
    }

    @Operation(summary = "Search addresses by neighborhood (unpaginated)", description = "Filter addresses by neighborhood without pagination")
    @GetMapping("/all/by-neighborhood")
    public ResponseEntity<List<AddressResponseDTO>> getByNeighborhood(@RequestParam String neighborhood) {
        return ResponseEntity.ok(addressService.getByNeighborhood(neighborhood));
    }

    @Operation(summary = "Search addresses by city and neighborhood (unpaginated)", description = "Filter addresses by city and neighborhood without pagination")
    @GetMapping("/all/by-city-and-neighborhood")
    public ResponseEntity<List<AddressResponseDTO>> getByCityAndNeighborhood(
            @RequestParam String city,
            @RequestParam String neighborhood) {
        return ResponseEntity.ok(addressService.getByCityAndNeighborhood(city, neighborhood));
    }

    @Operation(summary = "Search addresses by street (unpaginated)", description = "Filter addresses by street without pagination")
    @GetMapping("/all/by-street")
    public ResponseEntity<List<AddressResponseDTO>> getByStreet(@RequestParam String street) {
        return ResponseEntity.ok(addressService.getByStreet(street));
    }

    @Operation(summary = "Search addresses by city and street (unpaginated)", description = "Filter addresses by city and street without pagination")
    @GetMapping("/all/by-city-and-street")
    public ResponseEntity<List<AddressResponseDTO>> getByCityAndStreet(
            @RequestParam String city,
            @RequestParam String street) {
        return ResponseEntity.ok(addressService.getByCityAndStreet(city, street));
    }

    // PAGINATED SEARCH

    @Operation(summary = "Get all addresses (paginated)", description = "Retrieves all addresses with pagination")
    @GetMapping
    public ResponseEntity<Page<AddressResponseDTO>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(addressService.getAllPaged(pageable));
    }

    @Operation(summary = "Search addresses by city (paginated)")
    @GetMapping("/search/by-city")
    public ResponseEntity<Page<AddressResponseDTO>> getByCityPaged(
            @RequestParam String city, Pageable pageable) {
        return ResponseEntity.ok(addressService.getByCityPaged(city, pageable));
    }

    @Operation(summary = "Search addresses by state (paginated)")
    @GetMapping("/search/by-state")
    public ResponseEntity<Page<AddressResponseDTO>> getByStatePaged(
            @RequestParam String state, Pageable pageable) {
        return ResponseEntity.ok(addressService.getByStatePaged(state, pageable));
    }

    @Operation(summary = "Search addresses by neighborhood (paginated)")
    @GetMapping("/search/by-neighborhood")
    public ResponseEntity<Page<AddressResponseDTO>> getByNeighborhoodPaged(
            @RequestParam String neighborhood, Pageable pageable) {
        return ResponseEntity.ok(addressService.getByNeighborhoodPaged(neighborhood, pageable));
    }

    @Operation(summary = "Search addresses by city and neighborhood (paginated)")
    @GetMapping("/search/by-city-and-neighborhood")
    public ResponseEntity<Page<AddressResponseDTO>> getByCityAndNeighborhoodPaged(
            @RequestParam String city,
            @RequestParam String neighborhood,
            Pageable pageable) {
        return ResponseEntity.ok(addressService.getByCityAndNeighborhoodPaged(city, neighborhood, pageable));
    }

    @Operation(summary = "Search addresses by street (paginated)")
    @GetMapping("/search/by-street")
    public ResponseEntity<Page<AddressResponseDTO>> getByStreetPaged(
            @RequestParam String street, Pageable pageable) {
        return ResponseEntity.ok(addressService.getByStreetPaged(street, pageable));
    }

    @Operation(summary = "Search addresses by city and street (paginated)")
    @GetMapping("/search/by-city-and-street")
    public ResponseEntity<Page<AddressResponseDTO>> getByCityAndStreetPaged(
            @RequestParam String city,
            @RequestParam String street,
            Pageable pageable) {
        return ResponseEntity.ok(addressService.getByCityAndStreetPaged(city, street, pageable));
    }
}
