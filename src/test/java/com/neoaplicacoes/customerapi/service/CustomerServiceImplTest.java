package com.neoaplicacoes.customerapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.neoaplicacoes.customerapi.mapper.CustomerMapper;
import com.neoaplicacoes.customerapi.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customerapi.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customerapi.model.entity.Address;
import com.neoaplicacoes.customerapi.model.entity.Customer;
import com.neoaplicacoes.customerapi.repository.CustomerRepository;
import com.neoaplicacoes.customerapi.service.impl.CustomerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

/**
 * Unit tests for CustomerServiceImpl using Mockito. The repository and mapper are mocked to isolate
 * the service logic.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

  @Mock private CustomerRepository repository;

  @Mock private CustomerMapper mapper;

  @InjectMocks private CustomerServiceImpl service;

  private CustomerRequestDTO request;
  private Customer entity;
  private Customer saved;
  private CustomerResponseDTO response;

  @BeforeEach
  void setUp() {
    Address address = new Address();
    address.setId(1L);
    address.setCep("12345678");
    address.setNumber("100");
    address.setComplement("Apt 10");
    address.setStreet("Main St");
    address.setNeighborhood("Centro");
    address.setCity("São Paulo");
    address.setState("SP");

    entity = new Customer();
    entity.setId(1L);
    entity.setName("John Doe");
    entity.setEmail("john@doe.com");
    entity.setCpf("12345678901");
    entity.setPhone("11999999999");
    entity.setBirthDate(LocalDate.of(1990, 1, 1));
    entity.setAddress(address);

    saved = new Customer();
    saved.setId(1L);
    saved.setName(entity.getName());
    saved.setEmail(entity.getEmail());
    saved.setCpf(entity.getCpf());
    saved.setPhone(entity.getPhone());
    saved.setBirthDate(entity.getBirthDate());
    saved.setAddress(address);

    AddressRequestDTO addrReq =
        new AddressRequestDTO("12345678", "100", "Apt 10", "Main St", "Centro", "São Paulo", "SP");

    request =
        new CustomerRequestDTO(
            "John Doe",
            "john@doe.com",
            "12345678901",
            "11999999999",
            LocalDate.of(1990, 1, 1),
            addrReq);

    response =
        new CustomerResponseDTO(
            1L,
            "John Doe",
            "john@doe.com",
            "12345678901",
            "11999999999",
            LocalDate.of(1990, 1, 1), // birthDate
            34, // age (will be ignored in assertions that do not care)
            new AddressResponseDTO(
                1L, "12345678", "100", "Apt 10", "Main St", "Centro", "São Paulo", "SP"));

    // Generic stub: whenever mapper.toDto(any Customer) is called, build a DTO mirroring the
    // entity.
    lenient()
        .when(mapper.toDto(any(Customer.class)))
        .thenAnswer(
            inv -> {
              Customer c = inv.getArgument(0);
              Address a = c.getAddress();
              AddressResponseDTO aDto =
                  a == null
                      ? null
                      : new AddressResponseDTO(
                          a.getId(),
                          a.getCep(),
                          a.getNumber(),
                          a.getComplement(),
                          a.getStreet(),
                          a.getNeighborhood(),
                          a.getCity(),
                          a.getState());
              return new CustomerResponseDTO(
                  c.getId(),
                  c.getName(),
                  c.getEmail(),
                  c.getCpf(),
                  c.getPhone(),
                  c.getBirthDate(),
                  c.getAge(),
                  aDto);
            });
    lenient()
        .when(mapper.toDtoList(anyList()))
        .thenAnswer(
            inv -> {
              List<Customer> list = inv.getArgument(0);
              return list.stream().map(c -> mapper.toDto(c)).toList();
            });
  }

  // ---------- CRUD ----------

  @Test
  @DisplayName("create: should map request, save entity and return DTO")
  void create_success() {
    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(saved);

    CustomerResponseDTO result = service.create(request);

    assertThat(result.id()).isEqualTo(1L);
    verify(mapper).toEntity(request);
    verify(repository).save(entity);
    verify(mapper).toDto(saved);
  }

  @Test
  @DisplayName("update: should partially update fields and return DTO")
  void update_success() {
    // New partial data for update (only some fields changed)
    AddressRequestDTO addrUpdate =
        new AddressRequestDTO("87654321", null, null, "New St", "Bairro", "Rio", "RJ");
    CustomerRequestDTO updateReq =
        new CustomerRequestDTO("Jane", null, null, null, null, addrUpdate);

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

    CustomerResponseDTO result = service.update(1L, updateReq);

    assertThat(result.name()).isEqualTo("Jane");
    assertThat(result.address().cep()).isEqualTo("87654321");
    assertThat(result.address().street()).isEqualTo("New St");
    assertThat(result.address().city()).isEqualTo("Rio");
    assertThat(result.address().state()).isEqualTo("RJ");
    verify(repository).findById(1L);
    verify(repository).save(any(Customer.class));
  }

  @Test
  @DisplayName("update: should throw when id not found")
  void update_notFound() {
    when(repository.findById(99L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.update(99L, request))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Customer not found");
    verify(repository).findById(99L);
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("delete: should delete when id exists")
  void delete_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    service.delete(1L);
    verify(repository).delete(entity);
  }

  @Test
  @DisplayName("delete: should throw when id not found")
  void delete_notFound() {
    when(repository.findById(99L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(EntityNotFoundException.class);
    verify(repository, never()).delete(any());
  }

  @Test
  @DisplayName("getById: should return DTO when found")
  void getById_success() {
    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    CustomerResponseDTO result = service.getById(1L);
    assertThat(result.email()).isEqualTo("john@doe.com");
    verify(repository).findById(1L);
    verify(mapper).toDto(entity);
  }

  @Test
  @DisplayName("getById: should throw when not found")
  void getById_notFound() {
    when(repository.findById(2L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.getById(2L)).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("getAll: should map list to DTO list")
  void getAll_success() {
    when(repository.findAll()).thenReturn(List.of(entity, saved));
    List<CustomerResponseDTO> list = service.getAll();
    assertThat(list).hasSize(2);
    verify(mapper).toDtoList(anyList());
  }

  // ---------- Filtering (unpaged) ----------

  @Test
  void getByName_success() {
    when(repository.findByNameContainingIgnoreCase("john")).thenReturn(List.of(entity));
    List<CustomerResponseDTO> list = service.getByName("john");
    assertThat(list).hasSize(1);
  }

  @Test
  void getByEmail_success() {
    when(repository.findByEmailIgnoreCase("john@doe.com")).thenReturn(List.of(entity));
    assertThat(service.getByEmail("john@doe.com")).hasSize(1);
  }

  @Test
  void getByCpf_success() {
    when(repository.findByCpf("12345678901")).thenReturn(List.of(entity));
    assertThat(service.getByCpf("12345678901")).hasSize(1);
  }

  @Test
  void getByCity_success() {
    when(repository.findByAddressCityIgnoreCase("São Paulo")).thenReturn(List.of(entity));
    assertThat(service.getByCity("São Paulo")).hasSize(1);
  }

  @Test
  void getByState_success() {
    when(repository.findByAddressStateIgnoreCase("SP")).thenReturn(List.of(entity));
    assertThat(service.getByState("SP")).hasSize(1);
  }

  @Test
  void getByCityAndNeighborhood_success() {
    when(repository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase(
            "São Paulo", "Centro"))
        .thenReturn(List.of(entity));
    assertThat(service.getByCityAndNeighborhood("São Paulo", "Centro")).hasSize(1);
  }

  // ---------- Pagination ----------

  @Nested
  class Pagination {

    private Pageable pageable;
    private Page<Customer> page;

    @BeforeEach
    void initPage() {
      pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
      page = new PageImpl<>(List.of(entity), pageable, 1);
    }

    @Test
    void getAllPaged_success() {
      when(repository.findAll(pageable)).thenReturn(page);
      Page<CustomerResponseDTO> result = service.getAllPaged(pageable);
      assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getByNamePaged_success() {
      when(repository.findByNameContainingIgnoreCase("john", pageable)).thenReturn(page);
      assertThat(service.getByNamePaged("john", pageable).getContent()).hasSize(1);
    }

    @Test
    void getByEmailPaged_success() {
      when(repository.findByEmailContainingIgnoreCase("doe", pageable)).thenReturn(page);
      assertThat(service.getByEmailPaged("doe", pageable).getContent()).hasSize(1);
    }

    @Test
    void getByCpfPaged_success() {
      when(repository.findByCpfContaining("123", pageable)).thenReturn(page);
      assertThat(service.getByCpfPaged("123", pageable).getContent()).hasSize(1);
    }

    @Test
    void getByCityPaged_success() {
      when(repository.findByAddressCityIgnoreCase("São Paulo", pageable)).thenReturn(page);
      assertThat(service.getByCityPaged("São Paulo", pageable).getContent()).hasSize(1);
    }

    @Test
    void getByStatePaged_success() {
      when(repository.findByAddressStateIgnoreCase("SP", pageable)).thenReturn(page);
      assertThat(service.getByStatePaged("SP", pageable).getContent()).hasSize(1);
    }

    @Test
    void getByCityAndNeighborhoodPaged_success() {
      when(repository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase(
              "São Paulo", "Centro", pageable))
          .thenReturn(page);
      assertThat(
              service.getByCityAndNeighborhoodPaged("São Paulo", "Centro", pageable).getContent())
          .hasSize(1);
    }
  }
}
