package com.neoaplicacoes.customer_api.controller;

import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.service.AddressService;
import com.neoaplicacoes.customer_api.util.PaginationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    // -------------------- CRUD --------------------

    @Operation(summary = "Create address", description = "Creates a new address")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponseDTO> create(@Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO created = addressService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(created);
    }

    @Operation(summary = "Update address", description = "Updates an existing address by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO dto) {
        AddressResponseDTO updated = addressService.update(id, dto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updated);
    }

    @Operation(summary = "Delete address", description = "Deletes an address by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get address by ID", description = "Retrieves an address by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Long id) {
        AddressResponseDTO dto = addressService.getById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    // -------------------- UNPAGINATED SEARCH --------------------

    @Operation(summary = "Get all addresses (unpaginated)", description = "Retrieves all addresses without pagination")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getAll() {
        List<AddressResponseDTO> list = addressService.getAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by city (unpaginated)")
    @GetMapping(value = "/all/by-city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByCity(@RequestParam String city) {
        List<AddressResponseDTO> list = addressService.getByCity(city);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by state (unpaginated)")
    @GetMapping(value = "/all/by-state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByState(@RequestParam String state) {
        List<AddressResponseDTO> list = addressService.getByState(state);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by neighborhood (unpaginated)")
    @GetMapping(value = "/all/by-neighborhood", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByNeighborhood(@RequestParam String neighborhood) {
        List<AddressResponseDTO> list = addressService.getByNeighborhood(neighborhood);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by city and neighborhood (unpaginated)")
    @GetMapping(value = "/all/by-city-and-neighborhood", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByCityAndNeighborhood(
            @RequestParam String city,
            @RequestParam String neighborhood) {
        List<AddressResponseDTO> list = addressService.getByCityAndNeighborhood(city, neighborhood);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by street (unpaginated)")
    @GetMapping(value = "/all/by-street", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByStreet(@RequestParam String street) {
        List<AddressResponseDTO> list = addressService.getByStreet(street);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @Operation(summary = "Search addresses by city and street (unpaginated)")
    @GetMapping(value = "/all/by-city-and-street", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponseDTO>> getByCityAndStreet(
            @RequestParam String city,
            @RequestParam String street) {
        List<AddressResponseDTO> list = addressService.getByCityAndStreet(city, street);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }


    // -------------------- PAGINATED SEARCH --------------------

    @Operation(summary = "Get all addresses (paginated)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getAllPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> pageResult = addressService.getAllPaged(pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pageResult);
    }

    @Operation(summary = "Search addresses by city (paginated)")
    @GetMapping(value = "/search/by-city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByCityPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String city) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByCityPaged(city, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }

    @Operation(summary = "Search addresses by state (paginated)")
    @GetMapping(value = "/search/by-state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByStatePaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String state) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByStatePaged(state, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }

    @Operation(summary = "Search addresses by neighborhood (paginated)")
    @GetMapping(value = "/search/by-neighborhood", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByNeighborhoodPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String neighborhood) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByNeighborhoodPaged(neighborhood, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }

    @Operation(summary = "Search addresses by city and neighborhood (paginated)")
    @GetMapping(value = "/search/by-city-and-neighborhood", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByCityAndNeighborhoodPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String city,
            @RequestParam String neighborhood) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByCityAndNeighborhoodPaged(city, neighborhood, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }

    @Operation(summary = "Search addresses by street (paginated)")
    @GetMapping(value = "/search/by-street", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByStreetPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String street) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByStreetPaged(street, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }

    @Operation(summary = "Search addresses by city and street (paginated)")
    @GetMapping(value = "/search/by-city-and-street", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AddressResponseDTO>> getByCityAndStreetPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sorting criteria: property,asc|desc", example = "city,asc")
            @RequestParam(required = false) String sort,

            @RequestParam String city,
            @RequestParam String street) {

        Pageable pageable = PaginationUtil.createPageable(pageNumber, size, sort);
        Page<AddressResponseDTO> page = addressService.getByCityAndStreetPaged(city, street, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(page);
    }
}