package com.neoaplicacoes.customer_api.repository;

import com.neoaplicacoes.customer_api.model.entity.Address;
import com.neoaplicacoes.customer_api.model.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests using @DataJpaTest with H2.
 * Each query method is validated against deterministic seed data.
 */
@ActiveProfiles("test")
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    private Customer c1, c2, c3;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        c1 = customer("John Doe", "john@doe.com", "12345678901",
                "11911111111", "São Paulo", "SP", "Centro");
        c2 = customer("Jane Roe", "jane@roe.com", "22222222222",
                "11922222222", "Rio", "RJ", "Copacabana");
        c3 = customer("Joana Silva", "joana@silva.com", "33333333333",
                "21933333333", "São Paulo", "SP", "Moema");

        repository.saveAll(List.of(c1, c2, c3));
    }

    @Test
    @DisplayName("findByEmailIgnoreCase")
    void findByEmailIgnoreCase() {
        assertThat(repository.findByEmailIgnoreCase("JOHN@DOE.COM"))
                .extracting(Customer::getId).containsExactly(c1.getId());
    }

    @Test
    @DisplayName("findByCpf")
    void findByCpf() {
        assertThat(repository.findByCpf("22222222222"))
                .extracting(Customer::getId).containsExactly(c2.getId());
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase")
    void findByNameContainingIgnoreCase() {
        assertThat(repository.findByNameContainingIgnoreCase("jo"))
                .extracting(Customer::getId).containsExactlyInAnyOrder(c1.getId(), c3.getId());
    }

    @Test
    @DisplayName("findByEmailContainingIgnoreCase")
    void findByEmailContainingIgnoreCase() {
        assertThat(repository.findByEmailContainingIgnoreCase("roe"))
                .extracting(Customer::getId).containsExactly(c2.getId());
    }

    @Test
    @DisplayName("findByCpfContaining")
    void findByCpfContaining() {
        assertThat(repository.findByCpfContaining("333"))
                .extracting(Customer::getId).containsExactly(c3.getId());
    }

    @Test
    @DisplayName("findByName OR Email paged")
    void findByNameOrEmailPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase("jo", "roe", pageable);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("findByAddressCityIgnoreCase (list + page)")
    void findByAddressCityIgnoreCase() {
        assertThat(repository.findByAddressCityIgnoreCase("são paulo")).hasSize(2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = repository.findByAddressCityIgnoreCase("SÃO PAULO", pageable);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByAddressStateIgnoreCase (list + page)")
    void findByAddressStateIgnoreCase() {
        assertThat(repository.findByAddressStateIgnoreCase("sp")).hasSize(2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = repository.findByAddressStateIgnoreCase("SP", pageable);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase (list + page)")
    void findByCityAndNeighborhood() {
        assertThat(repository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase("São Paulo", "Centro"))
                .extracting(Customer::getId).containsExactly(c1.getId());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page =
                repository.findByAddressCityIgnoreCaseAndAddressNeighborhoodIgnoreCase("São Paulo", "Moema", pageable);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(c3.getId());
    }

    // --------- helpers ---------

    private static Customer customer(String name, String email, String cpf, String phone,
                                     String city, String state, String neighborhood) {
        Address a = new Address();
        a.setCep("12345678");
        a.setNumber("100");
        a.setComplement("Apt 10");
        a.setStreet("Main St");
        a.setNeighborhood(neighborhood);
        a.setCity(city);
        a.setState(state);

        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setCpf(cpf);
        c.setPhone(phone);
        c.setBirthDate(LocalDate.of(1990, 1, 1));
        c.setAddress(a);
        return c;
    }
}
