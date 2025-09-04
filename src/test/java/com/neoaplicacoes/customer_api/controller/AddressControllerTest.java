package com.neoaplicacoes.customer_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoaplicacoes.customer_api.exception.GlobalExceptionHandler;
import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AddressService service;

    private AddressRequestDTO sampleRequest() {
        return new AddressRequestDTO(
                "12345678", "100", "Apt 10", "Main St",
                "Centro", "São Paulo", "SP"
        );
    }

    private AddressResponseDTO sampleResponse() {
        return new AddressResponseDTO(
                1L, "12345678", "100", "Apt 10", "Main St",
                "Centro", "São Paulo", "SP"
        );
    }

    private Page<AddressResponseDTO> pageOf(AddressResponseDTO dto) {
        return new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
    }

    // ------------------ CRUD (ADMIN only) ------------------

    @Test
    @DisplayName("POST /api/addresses → 201 Created (ADMIN only)")
    @WithMockUser(roles = "ADMIN")
    void createAsAdmin() throws Exception {
        when(service.create(any())).thenReturn(sampleResponse());

        mvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/addresses/{id} → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void updateAsAdmin() throws Exception {
        when(service.update(anyLong(), any())).thenReturn(sampleResponse());

        mvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value("São Paulo"));
    }

    @Test
    @DisplayName("PUT /api/addresses/{id} → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void updateAsUserForbidden() throws Exception {
        mvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/addresses/{id} → 204 No Content for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void deleteAsAdmin() throws Exception {
        mvc.perform(delete("/api/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(service).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/addresses/{id} → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void deleteAsUserForbidden() throws Exception {
        mvc.perform(delete("/api/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // ------------------ GET endpoints (public) ------------------

    @Test
    @WithMockUser(roles = "USER")
    void getById() throws Exception {
        when(service.getById(anyLong())).thenReturn(sampleResponse());

        mvc.perform(get("/api/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.street").value("Main St"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_notFound() throws Exception {
        when(service.getById(anyLong()))
                .thenThrow(new EntityNotFoundException("Address not found with id 99"));

        mvc.perform(get("/api/addresses/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Address not found")));
    }

    // ------------------ PAGINATED ------------------

    @Test
    @WithMockUser(roles = "USER")
    void getAllPaged() throws Exception {
        when(service.getAllPaged(any(Pageable.class))).thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses?page=0&size=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "user@email.com", roles = {"USER"})
    void getByCityPaged() throws Exception {
        when(service.getByCityPaged(anyString(), any(Pageable.class))).thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-city?city=São Paulo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByStatePaged() throws Exception {
        when(service.getByStatePaged(anyString(), any(Pageable.class))).thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-state?state=SP")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByNeighborhoodPaged() throws Exception {
        when(service.getByNeighborhoodPaged(anyString(), any(Pageable.class))).thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-neighborhood?neighborhood=Centro")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCityAndNeighborhoodPaged() throws Exception {
        when(service.getByCityAndNeighborhoodPaged(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-city-and-neighborhood?city=São Paulo&neighborhood=Centro")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByStreetPaged() throws Exception {
        when(service.getByStreetPaged(anyString(), any(Pageable.class))).thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-street?street=Main")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCityAndStreetPaged() throws Exception {
        when(service.getByCityAndStreetPaged(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(pageOf(sampleResponse()));

        mvc.perform(get("/api/addresses/search/by-city-and-street?city=São Paulo&street=Main")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ------------------ UNPAGINATED ------------------

    @Test
    @WithMockUser(roles = "USER")
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCity() throws Exception {
        when(service.getByCity(anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-city?city=São Paulo")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByState() throws Exception {
        when(service.getByState(anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-state?state=SP")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByNeighborhood() throws Exception {
        when(service.getByNeighborhood(anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-neighborhood?neighborhood=Centro")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCityAndNeighborhood() throws Exception {
        when(service.getByCityAndNeighborhood(anyString(), anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-city-and-neighborhood?city=São Paulo&neighborhood=Centro")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByStreet() throws Exception {
        when(service.getByStreet(anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-street?street=Main")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCityAndStreet() throws Exception {
        when(service.getByCityAndStreet(anyString(), anyString())).thenReturn(List.of(sampleResponse()));

        mvc.perform(get("/api/addresses/all/by-city-and-street?city=São Paulo&street=Main")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
