package com.neoaplicacoes.customer_api.service.impl;

import com.neoaplicacoes.customer_api.model.dto.request.AddressRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.AddressResponseDTO;
import com.neoaplicacoes.customer_api.model.entity.Address;
import com.neoaplicacoes.customer_api.mapper.AddressMapper;
import com.neoaplicacoes.customer_api.repository.AddressRepository;
import com.neoaplicacoes.customer_api.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link AddressService}.
 * Provides CRUD operations, filtering, and pagination using AddressMapper (MapStruct).
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    // CRUD METHODS


    @Override
    public AddressResponseDTO create(AddressRequestDTO dto) {
        Address address = addressMapper.toEntity(dto);
        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    @Override
    public AddressResponseDTO update(Long id, AddressRequestDTO dto) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id " + id));
        updateAddressPartial(dto, existing);
        Address updated = addressRepository.save(existing);
        return addressMapper.toResponse(updated);
    }

    private void updateAddressPartial(AddressRequestDTO dto, Address entity) {
        if (dto.cep() != null) entity.setCep(dto.cep());
        if (dto.number() != null) entity.setNumber(dto.number());
        if (dto.complement() != null) entity.setComplement(dto.complement());
        if (dto.street() != null) entity.setStreet(dto.street());
        if (dto.neighborhood() != null) entity.setNeighborhood(dto.neighborhood());
        if (dto.city() != null) entity.setCity(dto.city());
        if (dto.state() != null) entity.setState(dto.state());
    }

    @Override
    public void delete(Long id) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id " + id));
        addressRepository.delete(existing);
    }

    @Override
    public AddressResponseDTO getById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id " + id));
    }

    @Override
    public List<AddressResponseDTO> getAll() {
        return addressMapper.toResponseList(addressRepository.findAll());
    }

    /**
     * Retrieves all addresses with pagination.
     *
     * @param pageable pagination information
     * @return paged result of addresses
     */
    @Override
    public Page<AddressResponseDTO> getAllPaged(Pageable pageable) {
        return addressRepository.findAll(pageable)
                .map(addressMapper::toResponse);
    }


    // FILTERS WITHOUT PAGINATION

    @Override
    public List<AddressResponseDTO> getByCity(String city) {
        return addressMapper.toResponseList(addressRepository.findByCityIgnoreCase(city));
    }

    @Override
    public List<AddressResponseDTO> getByState(String state) {
        return addressMapper.toResponseList(addressRepository.findByStateIgnoreCase(state));
    }

    @Override
    public List<AddressResponseDTO> getByNeighborhood(String neighborhood) {
        return addressMapper.toResponseList(addressRepository.findByNeighborhoodIgnoreCase(neighborhood));
    }

    @Override
    public List<AddressResponseDTO> getByCityAndNeighborhood(String city, String neighborhood) {
        return addressMapper.toResponseList(addressRepository.findByCityIgnoreCaseAndNeighborhoodIgnoreCase(city, neighborhood));
    }

    @Override
    public List<AddressResponseDTO> getByCep(String cep) {
        return addressRepository.findByCep(cep)
                .map(addressMapper::toResponse)
                .stream()
                .toList();
    }

    @Override
    public List<AddressResponseDTO> getByCepAndState(String cep, String state) {
        return addressMapper.toResponseList(addressRepository.findByCepAndStateIgnoreCase(cep, state));
    }

    @Override
    public List<AddressResponseDTO> getByStreet(String street) {
        return addressMapper.toResponseList(addressRepository.findByStreetContainingIgnoreCase(street));
    }

    @Override
    public List<AddressResponseDTO> getByCityAndStreet(String city, String street) {
        return addressMapper.toResponseList(addressRepository.findByCityIgnoreCaseAndStreetContainingIgnoreCase(city, street));
    }

    // FILTERS WITH PAGINATION


    @Override
    public Page<AddressResponseDTO> getByCityPaged(String city, Pageable pageable) {
        return addressRepository.findByCityIgnoreCase(city, pageable)
                .map(addressMapper::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> getByStatePaged(String state, Pageable pageable) {
        return addressRepository.findByStateIgnoreCase(state, pageable)
                .map(addressMapper::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> getByNeighborhoodPaged(String neighborhood, Pageable pageable) {
        return addressRepository.findByNeighborhoodIgnoreCase(neighborhood, pageable)
                .map(addressMapper::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> getByCityAndNeighborhoodPaged(String city, String neighborhood, Pageable pageable) {
        return addressRepository.findByCityIgnoreCaseAndNeighborhoodIgnoreCase(city, neighborhood, pageable)
                .map(addressMapper::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> getByStreetPaged(String street, Pageable pageable) {
        return addressRepository.findByStreetContainingIgnoreCase(street, pageable)
                .map(addressMapper::toResponse);
    }

    @Override
    public Page<AddressResponseDTO> getByCityAndStreetPaged(String city, String street, Pageable pageable) {
        return addressRepository.findByCityIgnoreCaseAndStreetContainingIgnoreCase(city, street, pageable)
                .map(addressMapper::toResponse);
    }
}
