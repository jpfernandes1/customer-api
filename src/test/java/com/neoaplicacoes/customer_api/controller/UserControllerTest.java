package com.neoaplicacoes.customer_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoaplicacoes.customer_api.exception.GlobalExceptionHandler;
import com.neoaplicacoes.customer_api.model.dto.request.UserAdminRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.model.enums.Role;
import com.neoaplicacoes.customer_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;

    private UserRequestDTO sampleRequest;
    private UserResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
        sampleRequest = new UserRequestDTO("user@example.com", "password123");
        sampleResponse = new UserResponseDTO(
                1L,
                "user@example.com",
                "ROLE_USER",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // ----------- PUBLIC REGISTER -----------

    @Test
    @DisplayName("POST /api/users/register → 201 Created")
    void register() throws Exception {
        when(userService.register(any())).thenReturn(sampleResponse);

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(sampleResponse.email()));
    }

    // ----------- ADMIN ACTIONS -----------

    @Test
    @DisplayName("POST /api/users → 201 Created for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void createByAdmin() throws Exception {
        when(userService.create(any())).thenReturn(sampleResponse);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(sampleResponse.email()));
    }

    @Test
    @DisplayName("POST /api/users → 403 Forbidden for USER")
    @WithMockUser(username = "user", roles = {"USER"})
    void createByUserForbidden() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/users/{id} → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void updateByAdmin() throws Exception {
        when(userService.update(anyLong(), any())).thenReturn(sampleResponse);

        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /api/users/{id} → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void updateByUserForbidden() throws Exception {
        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → 204 No Content for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void deleteByAdmin() throws Exception {
        doNothing().when(userService).delete(anyLong());

        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void deleteByUserForbidden() throws Exception {
        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PATCH /api/users/{id}/admin → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void patchAdminByAdmin() throws Exception {
        UserAdminRequestDTO patchDto = new UserAdminRequestDTO(Role.ROLE_ADMIN, true);
        when(userService.updateAdmin(anyLong(), any())).thenReturn(sampleResponse);

        mvc.perform(patch("/api/users/1/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PATCH /api/users/{id}/admin → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void patchAdminByUserForbidden() throws Exception {
        UserAdminRequestDTO patchDto = new UserAdminRequestDTO(Role.ROLE_USER, true);

        mvc.perform(patch("/api/users/1/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchDto)))
                .andExpect(status().isForbidden());
    }

    // ----------- GET ENDPOINTS -----------

    @Test
    @DisplayName("GET /api/users → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void getAllAsAdmin() throws Exception {
        when(userService.getAll()).thenReturn(List.of(sampleResponse));

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(sampleResponse.email()));
    }

    @Test
    @DisplayName("GET /api/users → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void getAllAsUserForbidden() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    // ------------------- PAGINATED -------------------

    @Test
    @DisplayName("GET /api/users/paged → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void getAllPagedAsAdmin() throws Exception {
        Page<UserResponseDTO> page = new PageImpl<>(List.of(sampleResponse));

        when(userService.getAllPaged(any()))
                .thenReturn(page);

        mvc.perform(get("/api/users/paged?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value(sampleResponse.email()));
    }


    @Test
    @DisplayName("GET /api/users/paged → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void getAllPagedAsUserForbidden() throws Exception {
        mvc.perform(get("/api/users/paged?page=0&size=10"))
                .andExpect(status().isForbidden());
    }

// ------------------- FILTERS -------------------

    @Test
    @DisplayName("GET /api/users/search/by-role → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void getByRoleAsAdmin() throws Exception {
        when(userService.getByRolePaged(any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleResponse), PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/users/search/by-role?role=ROLE_USER")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].role").value("ROLE_USER"));
    }


    @Test
    @DisplayName("GET /api/users/search/by-role → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void getByRoleAsUserForbidden() throws Exception {
        mvc.perform(get("/api/users/search/by-role?role=ROLE_USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/users/search/by-email → 200 OK for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void getByEmailAsAdmin() throws Exception {
        when(userService.getByEmailPaged(any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleResponse), PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/users/search/by-email?email=user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("user@example.com"));
    }



    @Test
    @DisplayName("GET /api/users/search/by-email → 403 Forbidden for USER")
    @WithMockUser(roles = "USER")
    void getByEmailAsUserForbidden() throws Exception {
        mvc.perform(get("/api/users/search/by-email?email=user@example.com"))
                .andExpect(status().isForbidden());
    }

}
