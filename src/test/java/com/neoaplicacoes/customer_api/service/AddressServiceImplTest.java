package com.neoaplicacoes.customer_api.service;

import com.neoaplicacoes.customer_api.mapper.AddressMapper;
import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.model.entity.Address;
import com.neoaplicacoes.customer_api.repository.AddressRepository;
import com.neoaplicacoes.customer_api.service.impl.AddressServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private AddressMapper mapper;

    private Address sampleEntity() {
        Address a = new Address();
        a.setId(1L);
        a.setCep("12345678");
        a.setNumber("100");
        a.setComplement("Apt 10");
        a.setStreet("Main St");
        a.setNeighborhood("Centro");
        a.setCity("São Paulo");
        a.setState("SP");
        return a;
    }

    private AddressResponseDTO sampleResponse() {
        return new AddressResponseDTO(1L,"12345678","100","Apt 10","Main St",
                "Centro","São Paulo","SP");
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new AddressServiceImpl(repository, mapper);
    }

    // ------------------- CRUD -------------------

    @Test
    @DisplayName("Create Address")
    void create() {
        AddressRequestDTO dto = new AddressRequestDTO("12345678","100","Apt 10",
                "Main St","Centro","São Paulo","SP");
        Address entity = sampleEntity();
        AddressResponseDTO response = sampleResponse();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        AddressResponseDTO result = service.create(dto);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Update Address - success")
    void update() {
        AddressRequestDTO dto = new AddressRequestDTO("87654321","200","Apt 20",
                "Second St","Bairro","Rio","RJ");
        Address entity = sampleEntity();
        AddressResponseDTO response = sampleResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        AddressResponseDTO result = service.update(1L, dto);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Update Address - not found")
    void updateNotFound() {
        AddressRequestDTO dto = new AddressRequestDTO("87654321","200","Apt 20",
                "Second St","Bairro","Rio","RJ");
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(99L, dto));
    }

    @Test
    @DisplayName("Delete Address - success")
    void delete() {
        Address entity = sampleEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.delete(1L);
        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("Delete Address - not found")
    void deleteNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.delete(99L));
    }

    @Test
    @DisplayName("Get Address by ID - success")
    void getById() {
        Address entity = sampleEntity();
        AddressResponseDTO response = sampleResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        AddressResponseDTO result = service.getById(1L);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("Get Address by ID - not found")
    void getByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("Get all addresses")
    void getAll() {
        Address entity = sampleEntity();
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

        List<AddressResponseDTO> result = service.getAll();
        assertThat(result).hasSize(1);
    }

    // ------------------- FILTERS NON-PAGINATED -------------------

    @Nested
    class FiltersNonPaged {

        @Test
        void getByCity() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCase("São Paulo")).thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByCity("São Paulo");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByState() {
            Address entity = sampleEntity();
            when(repository.findByStateIgnoreCase("SP")).thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByState("SP");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByNeighborhood() {
            Address entity = sampleEntity();
            when(repository.findByNeighborhoodIgnoreCase("Centro")).thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByNeighborhood("Centro");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByCityAndNeighborhood() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCaseAndNeighborhoodIgnoreCase("São Paulo","Centro"))
                    .thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByCityAndNeighborhood("São Paulo","Centro");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByCep() {
            Address entity = sampleEntity();
            when(repository.findByCep("12345678")).thenReturn(Optional.of(entity));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            List<AddressResponseDTO> result = service.getByCep("12345678");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByCepAndState() {
            Address entity = sampleEntity();
            when(repository.findByCepAndStateIgnoreCase("12345678","SP")).thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByCepAndState("12345678","SP");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByStreet() {
            Address entity = sampleEntity();
            when(repository.findByStreetContainingIgnoreCase("Main")).thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByStreet("Main");
            assertThat(result).hasSize(1);
        }

        @Test
        void getByCityAndStreet() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCaseAndStreetContainingIgnoreCase("São Paulo","Main"))
                    .thenReturn(List.of(entity));
            when(mapper.toResponseList(List.of(entity))).thenReturn(List.of(sampleResponse()));

            List<AddressResponseDTO> result = service.getByCityAndStreet("São Paulo","Main");
            assertThat(result).hasSize(1);
        }
    }

    // ------------------- FILTERS PAGINATED -------------------

    @Nested
    class FiltersPaged {

        @Test
        void getByCityPaged() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCase("São Paulo", PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByCityPaged("São Paulo", PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void getByStatePaged() {
            Address entity = sampleEntity();
            when(repository.findByStateIgnoreCase("SP", PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByStatePaged("SP", PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void getByNeighborhoodPaged() {
            Address entity = sampleEntity();
            when(repository.findByNeighborhoodIgnoreCase("Centro", PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByNeighborhoodPaged("Centro", PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void getByCityAndNeighborhoodPaged() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCaseAndNeighborhoodIgnoreCase("São Paulo","Centro",PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByCityAndNeighborhoodPaged("São Paulo","Centro",PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void getByStreetPaged() {
            Address entity = sampleEntity();
            when(repository.findByStreetContainingIgnoreCase("Main",PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByStreetPaged("Main",PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void getByCityAndStreetPaged() {
            Address entity = sampleEntity();
            when(repository.findByCityIgnoreCaseAndStreetContainingIgnoreCase("São Paulo","Main",PageRequest.of(0,10)))
                    .thenReturn(new PageImpl<>(List.of(entity)));
            when(mapper.toResponse(entity)).thenReturn(sampleResponse());

            var page = service.getByCityAndStreetPaged("São Paulo","Main",PageRequest.of(0,10));
            assertThat(page.getContent()).hasSize(1);
        }
    }
}
