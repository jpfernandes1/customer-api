package com.neoaplicacoes.customerapi.service.impl;

import com.neoaplicacoes.customerapi.mapper.CustomerMapper;
import com.neoaplicacoes.customerapi.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.request.CustomerRequestDTO;
import com.neoaplicacoes.customerapi.model.dto.response.CustomerResponseDTO;
import com.neoaplicacoes.customerapi.model.entity.Address;
import com.neoaplicacoes.customerapi.model.entity.Customer;
import com.neoaplicacoes.customerapi.repository.CustomerRepository;
import com.neoaplicacoes.customerapi.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CustomerService}. Provides CRUD operations, filtering, and pagination
 * using CustomerMapper (MapStruct).
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Autowired
  public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
  }

  // CRUD methods

  @Override
  public CustomerResponseDTO create(CustomerRequestDTO dto) {
    Customer customer = customerMapper.toEntity(dto);
    Customer saved = customerRepository.save(customer);
    return customerMapper.toDto(saved);
  }

  @Override
  public CustomerResponseDTO update(Long id, CustomerRequestDTO dto) {
    Customer existing =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));
    updateCustomerPartial(dto, existing);
    Customer updated = customerRepository.save(existing);
    return customerMapper.toDto(updated);
  }

  private void updateCustomerPartial(CustomerRequestDTO dto, Customer entity) {
    // Campos simples do Customer
    if (dto.name() != null) {
      entity.setName(dto.name());
    }
    if (dto.email() != null) {
      entity.setEmail(dto.email());
    }
    if (dto.cpf() != null) {
      entity.setCpf(dto.cpf());
    }
    if (dto.phone() != null) {
      entity.setPhone(dto.phone());
    }
    if (dto.birthDate() != null) {
      entity.setBirthDate(dto.birthDate());
    }

    // Tratamento especial para o Address (embedded object)
    if (dto.address() != null) {
      updateAddressPartial(dto.address(), entity.getAddress());
    }
  }

  private void updateAddressPartial(AddressRequestDTO dto, Address entity) {
    if (dto.cep() != null) {
      entity.setCep(dto.cep());
    }
    if (dto.number() != null) {
      entity.setNumber(dto.number());
    }
    if (dto.complement() != null) {
      entity.setComplement(dto.complement());
    }
    if (dto.street() != null) {
      entity.setStreet(dto.street());
    }
    if (dto.neighborhood() != null) {
      entity.setNeighborhood(dto.neighborhood());
    }
    if (dto.city() != null) {
      entity.setCity(dto.city());
    }
    if (dto.state() != null) {
      entity.setState(dto.state());
    }
  }

  @Override
  public void delete(Long id) {
    Customer existing =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));
    customerRepository.delete(existing);
  }

  @Override
  public CustomerResponseDTO getById(Long id) {
    return customerRepository
        .findById(id)
        .map(customerMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));
  }

  @Override
  public List<CustomerResponseDTO> getAll() {
    return customerMapper.toDtoList(customerRepository.findAll());
  }

  // Filtering methods

  @Override
  public List<CustomerResponseDTO> getByName(String name) {
    List<Customer> list = customerRepository.findByNameContainingIgnoreCase(name);
    return customerMapper.toDtoList(list);
  }

  @Override
  public List<CustomerResponseDTO> getByEmail(String email) {
    List<Customer> list = customerRepository.findByEmailIgnoreCase(email);
    return customerMapper.toDtoList(list);
  }

  @Override
  public List<CustomerResponseDTO> getByCpf(String cpf) {
    List<Customer> list = customerRepository.findByCpf(cpf);
    return customerMapper.toDtoList(list);
  }

  @Override
  public List<CustomerResponseDTO> getByCity(String city) {
    List<Customer> list = customerRepository.findByAddressCityIgnoreCase(city);
    return customerMapper.toDtoList(list);
  }

  @Override
  public List<CustomerResponseDTO> getByState(String state) {
    List<Customer> list = customerRepository.findByAddressStateIgnoreCase(state);
    return customerMapper.toDtoList(list);
  }

  @Override
  public List<CustomerResponseDTO> getByCityAndNeighborhood(String city, String neighborhood) {
    List<Customer> list =
        customerRepository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase(
            city, neighborhood);
    return customerMapper.toDtoList(list);
  }

  // Pagination methods

  @Override
  public Page<CustomerResponseDTO> getAllPaged(Pageable pageable) {
    return customerRepository.findAll(pageable).map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByNamePaged(String name, Pageable pageable) {
    return customerRepository
        .findByNameContainingIgnoreCase(name, pageable)
        .map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByEmailPaged(String email, Pageable pageable) {
    return customerRepository
        .findByEmailContainingIgnoreCase(email, pageable)
        .map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByCpfPaged(String cpf, Pageable pageable) {
    return customerRepository.findByCpfContaining(cpf, pageable).map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByCityPaged(String city, Pageable pageable) {
    return customerRepository
        .findByAddressCityIgnoreCase(city, pageable)
        .map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByStatePaged(String state, Pageable pageable) {
    return customerRepository
        .findByAddressStateIgnoreCase(state, pageable)
        .map(customerMapper::toDto);
  }

  @Override
  public Page<CustomerResponseDTO> getByCityAndNeighborhoodPaged(
      String city, String neighborhood, Pageable pageable) {
    Page<Customer> page =
        customerRepository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase(
            city, neighborhood, pageable);
    return page.map(customerMapper::toDto);
  }
}
