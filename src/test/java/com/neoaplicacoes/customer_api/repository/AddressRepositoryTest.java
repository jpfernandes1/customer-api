package com.neoaplicacoes.customer_api.repository;

import com.neoaplicacoes.customer_api.model.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {

    @Autowired
    private AddressRepository repository;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        address1 = new Address();
        address1.setCep("12345678");
        address1.setNumber("100");
        address1.setComplement("Apt 10");
        address1.setStreet("Main St");
        address1.setNeighborhood("Centro");
        address1.setCity("São Paulo");
        address1.setState("SP");

        address2 = new Address();
        address2.setCep("87654321");
        address2.setNumber("200");
        address2.setComplement("Apt 20");
        address2.setStreet("Second St");
        address2.setNeighborhood("Bairro");
        address2.setCity("Rio");
        address2.setState("RJ");

        repository.saveAll(List.of(address1, address2));
    }

    // ------------------ FIND METHODS ------------------

    @Test
    @DisplayName("Find by city ignoring case → positive")
    void findByCityIgnoreCase() {
        List<Address> result = repository.findByCityIgnoreCase("são paulo");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by city ignoring case → not found")
    void findByCityIgnoreCase_notFound() {
        List<Address> result = repository.findByCityIgnoreCase("Curitiba");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Find by state ignoring case → positive")
    void findByStateIgnoreCase() {
        List<Address> result = repository.findByStateIgnoreCase("sp");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by state ignoring case → multiple results")
    void findByStateIgnoreCase_multiple() {
        Address address3 = new Address();
        address3.setCep("99999999");
        address3.setNumber("300");
        address3.setStreet("Third St");
        address3.setNeighborhood("Centro");
        address3.setCity("São Paulo");
        address3.setState("SP");
        repository.save(address3);

        List<Address> result = repository.findByStateIgnoreCase("SP");
        assertThat(result).hasSize(2).contains(address1, address3);
    }

    @Test
    @DisplayName("Find by neighborhood ignoring case")
    void findByNeighborhoodIgnoreCase() {
        List<Address> result = repository.findByNeighborhoodIgnoreCase("centro");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by city and neighborhood ignoring case")
    void findByCityAndNeighborhoodIgnoreCase() {
        List<Address> result = repository.findByCityIgnoreCaseAndNeighborhoodIgnoreCase("são paulo", "centro");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by CEP → positive")
    void findByCep() {
        Optional<Address> result = repository.findByCep("12345678");
        assertThat(result).isPresent().contains(address1);
    }

    @Test
    @DisplayName("Find by CEP → not found")
    void findByCep_notFound() {
        Optional<Address> result = repository.findByCep("00000000");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Find by CEP and state ignoring case")
    void findByCepAndStateIgnoreCase() {
        List<Address> result = repository.findByCepAndStateIgnoreCase("12345678", "sp");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by street containing ignoring case → positive")
    void findByStreetContainingIgnoreCase() {
        List<Address> result = repository.findByStreetContainingIgnoreCase("main");
        assertThat(result).hasSize(1).contains(address1);
    }

    @Test
    @DisplayName("Find by street containing ignoring case → multiple/no match")
    void findByStreetContainingIgnoreCase_edgeCases() {
        List<Address> resultEmpty = repository.findByStreetContainingIgnoreCase("xyz");
        assertThat(resultEmpty).isEmpty();

        Address address3 = new Address();
        address3.setCep("11111111");
        address3.setNumber("300");
        address3.setStreet("Main Avenue");
        address3.setNeighborhood("Centro");
        address3.setCity("São Paulo");
        address3.setState("SP");
        repository.save(address3);

        List<Address> resultMultiple = repository.findByStreetContainingIgnoreCase("main");
        assertThat(resultMultiple).hasSize(2).contains(address1, address3);
    }

    @Test
    @DisplayName("Find by city and street ignoring case")
    void findByCityAndStreetContainingIgnoreCase() {
        List<Address> result = repository.findByCityIgnoreCaseAndStreetContainingIgnoreCase("são paulo", "main");
        assertThat(result).hasSize(1).contains(address1);
    }

    // ------------------ CRUD METHODS ------------------

    @Test
    @DisplayName("Save new address")
    void saveAddress() {
        Address newAddr = new Address();
        newAddr.setCep("55555555");
        newAddr.setNumber("400");
        newAddr.setStreet("Fourth St");
        newAddr.setNeighborhood("Jardim");
        newAddr.setCity("Belo Horizonte");
        newAddr.setState("MG");

        Address saved = repository.save(newAddr);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("Delete address")
    void deleteAddress() {
        repository.delete(address1);
        assertThat(repository.findById(address1.getId())).isEmpty();
    }

    @Test
    @DisplayName("Update address")
    void updateAddress() {
        address2.setCity("Niterói");
        Address updated = repository.save(address2);
        assertThat(updated.getCity()).isEqualTo("Niterói");
    }
}
