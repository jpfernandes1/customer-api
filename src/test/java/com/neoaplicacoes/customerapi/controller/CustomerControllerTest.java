package com.neoaplicacoes.customerapi.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoaplicacoes.customerapi.exception.GlobalExceptionHandler;
import com.neoaplicacoes.customerapi.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customerapi.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customerapi.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @MockitoBean private CustomerService service;

  private CustomerRequestDTO sampleRequest() {
    AddressRequestDTO addr =
        new AddressRequestDTO("12345678", "100", "Apt 10", "Main St", "Centro", "São Paulo", "SP");
    return new CustomerRequestDTO(
        "John", "john@doe.com", "12345678901", "11999999999", LocalDate.of(1990, 1, 1), addr);
  }

  private CustomerResponseDTO sampleResponse() {
    return new CustomerResponseDTO(
        1L,
        "John",
        "john@doe.com",
        "12345678901",
        "11999999999",
        LocalDate.of(1990, 1, 1),
        34,
        new AddressResponseDTO(
            1L, "12345678", "100", "Apt 10", "Main St", "Centro", "São Paulo", "SP"));
  }

  // ---------- CRUD ----------

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/customers → 201 Created")
  void create() throws Exception {
    when(service.create(any())).thenReturn(sampleResponse());

    mvc.perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sampleRequest())))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("PUT /api/customers/{id} → 200 OK")
  void update() throws Exception {
    when(service.update(eq(1L), any())).thenReturn(sampleResponse());

    mvc.perform(
            put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sampleRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("john@doe.com"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("DELETE /api/customers/{id} → 204 No Content")
  void deleteCustomer() throws Exception {
    mvc.perform(delete("/api/customers/1")).andExpect(status().isNoContent());
    Mockito.verify(service).delete(1L);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/customers/{id} → 200 OK")
  void getById() throws Exception {
    when(service.getById(1L)).thenReturn(sampleResponse());
    mvc.perform(get("/api/customers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/customers/{id} → 404 when not found")
  void getById_notFound() throws Exception {
    when(service.getById(99L))
        .thenThrow(new EntityNotFoundException("Customer not found with id 99"));
    mvc.perform(get("/api/customers/99"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", containsString("Customer not found")));
  }

  // ---------- Paginated ----------

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/customers (paged) → 200 with content")
  void getAllPaged() throws Exception {
    Page<CustomerResponseDTO> page =
        new PageImpl<>(List.of(sampleResponse()), PageRequest.of(0, 10), 1);
    when(service.getAllPaged(any())).thenReturn(page);

    mvc.perform(get("/api/customers?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByNamePaged() throws Exception {
    when(service.getByNamePaged(eq("john"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(get("/api/customers/search/by-name?name=john"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].email").value("john@doe.com"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByEmailPaged() throws Exception {
    when(service.getByEmailPaged(eq("doe"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(get("/api/customers/search/by-email?email=doe"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCpfPaged() throws Exception {
    when(service.getByCpfPaged(eq("123"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(get("/api/customers/search/by-cpf?cpf=123")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCityPaged() throws Exception {
    when(service.getByCityPaged(eq("São Paulo"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(get("/api/customers/search/by-city?city=São Paulo")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByStatePaged() throws Exception {
    when(service.getByStatePaged(eq("SP"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(get("/api/customers/search/by-state?state=SP")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCityAndNeighborhoodPaged() throws Exception {
    when(service.getByCityAndNeighborhoodPaged(eq("São Paulo"), eq("Centro"), any()))
        .thenReturn(new PageImpl<>(List.of(sampleResponse())));
    mvc.perform(
            get(
                "/api/customers/search/by-city-and-neighborhood?city=São Paulo&neighborhood=Centro"))
        .andExpect(status().isOk());
  }

  // ---------- Unpaginated ----------

  @Test
  @WithMockUser(roles = "ADMIN")
  void getAll() throws Exception {
    when(service.getAll()).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByName() throws Exception {
    when(service.getByName("john")).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all/by-name?name=john")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByEmail() throws Exception {
    when(service.getByEmail("john@doe.com")).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all/by-email?email=john@doe.com")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCpf() throws Exception {
    when(service.getByCpf("12345678901")).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all/by-cpf?cpf=12345678901")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCity() throws Exception {
    when(service.getByCity("São Paulo")).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all/by-city?city=São Paulo")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByState() throws Exception {
    when(service.getByState("SP")).thenReturn(List.of(sampleResponse()));
    mvc.perform(get("/api/customers/all/by-state?state=SP")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getByCityAndNeighborhood() throws Exception {
    when(service.getByCityAndNeighborhood("São Paulo", "Centro"))
        .thenReturn(List.of(sampleResponse()));
    mvc.perform(
            get("/api/customers/all/by-city-and-neighborhood?city=São Paulo&neighborhood=Centro"))
        .andExpect(status().isOk());
  }

  // ---------- ADMIN ROLE PROTECTION ----------

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("PUT /api/customers/{id} → 403 Forbidden for non-admin")
  void updateForbiddenForUser() throws Exception {
    mvc.perform(
            put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sampleRequest())))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  @DisplayName("DELETE /api/customers/{id} → 403 Forbidden for non-admin")
  void deleteForbiddenForUser() throws Exception {
    mvc.perform(delete("/api/customers/1")).andExpect(status().isForbidden());
  }
}
