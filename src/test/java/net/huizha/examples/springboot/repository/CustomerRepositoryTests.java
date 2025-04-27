package net.huizha.examples.springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.huizha.examples.springboot.model.customer.Customer;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findById_should_returnCustomer_whenIdExists() {
        Customer customer = new Customer("Ming", "Li");
        customerRepository.save(customer);
        customer.setId(1L);
        Optional<Customer> expected = Optional.of(customer);
        assertThat(customerRepository.findById(1L)).isInstanceOf(Optional.class).isPresent().isEqualTo(expected);
        assertThat(customerRepository.findById(2L)).isEmpty();
    }

    @Test
    void save_should_autoIncreaseId_whenSavingMultipleCustomers() {
        Customer customer1 = (new Customer("Ming", "Li"));
        customerRepository.save(customer1);
        customer1.setId(1L);
        Customer customer2 = (new Customer("Hong", "Wang"));
        customerRepository.save(customer2);
        customer2.setId(2L);
        assertThat(customerRepository.findById(1L)).contains(customer1);
        Optional<Customer> expectedOptionalCustomer1 = Optional.of(customer1);
        assertThat(customerRepository.findById(1L)).isInstanceOf(Optional.class).isPresent()
                .isEqualTo(expectedOptionalCustomer1);
        assertThat(customerRepository.findById(2L)).contains(customer2);
        Optional<Customer> expectedOptionalCustomer2 = Optional.of(customer2);
        assertThat(customerRepository.findById(2L)).isInstanceOf(Optional.class).isPresent()
                .isEqualTo(expectedOptionalCustomer2);
    }

    @Test
    void findAll_should_returnAllCustomers_whenCalled() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Hong", "Wang"));
        assertThat(customerRepository.findAll()).isInstanceOf(List.class).hasSize(2);
    }

    @Test
    void deleteById_should_removeCustomer_whenIdExists() {
        assertThat(customerRepository.count()).isZero();
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Hong", "Wang"));
        assertThat(customerRepository.count()).isEqualTo(2);
        customerRepository.deleteById(2L);
        assertThat(customerRepository.count()).isEqualTo(1);
        assertThat(customerRepository.findAll()).isInstanceOf(List.class).hasSize(1);
        Customer customer = (new Customer("Ming", "Li")).setId(1L);
        Optional<Customer> expected = Optional.of(customer);
        assertThat(customerRepository.findById(1L)).isInstanceOf(Optional.class).isPresent().isEqualTo(expected);
        assertThat(customerRepository.findById(2L)).isEmpty();
    }

    @Test
    void deleteById_should_notThrowException_whenIdNotExist() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Hong", "Wang"));
        assertThat(customerRepository.findAll()).isInstanceOf(List.class).hasSize(2);
        assertDoesNotThrow(() -> {
            customerRepository.deleteById(3L);
        });
        assertThat(customerRepository.findAll()).isInstanceOf(List.class).hasSize(2);
    }

    @Test
    void findByFirstName_should_returnMatchingCustomers_whenValidNameGiven() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Lei", "Li"));
        assertThat(customerRepository.findByFirstName("Ming")).hasSize(1);
    }

    @Test
    void findByFirstName_should_throwConstraintViolation_whenFirstNameIsNullOrBlank() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Lei", "Li"));
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            customerRepository.findByFirstName(null);
        }).withMessageMatching("findByFirstName.firstName: must not be blank");
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            customerRepository.findByFirstName("");
        }).withMessageMatching("findByFirstName.firstName: must not be blank");
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            customerRepository.findByFirstName(" ");
        }).withMessageMatching("findByFirstName.firstName: must not be blank");
    }

    @Test
    void findByLastNameOrderByFirstNameAsc_should_returnOrderedCustomers_whenLastNameMatches() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Mei", "Li"));
        customerRepository.save(new Customer("An", "Li"));
        customerRepository.save(new Customer("Hong", "Wang"));
        customerRepository.save(new Customer("Zheng", "Li"));
        List<Customer> resultList = customerRepository.findByLastNameOrderByFirstNameAsc("Li");
        LOGGER.info(resultList.toString());
        assertThat(resultList).hasSize(4);
        assertThat(resultList.get(0).getFirstName()).isEqualTo("An");
    }

    @Test
    void findByFirstNameStartingWith_should_returnMatchingCustomers_whenOneLetterPrefixGiven() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Mei", "Li"));
        customerRepository.save(new Customer("Meng", "Li"));
        customerRepository.save(new Customer("An", "Li"));
        customerRepository.save(new Customer("Mao", "Wang"));
        customerRepository.save(new Customer("Zheng", "Li"));
        List<Customer> resultList = customerRepository.findByFirstNameStartingWith("M");
        LOGGER.info(resultList.toString());
        assertThat(resultList).hasSize(4);
    }

    @Test
    void findByFirstNameStartingWith_should_returnMatchingCustomers_whenTwoLetterPrefixGiven() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Mei", "Li"));
        customerRepository.save(new Customer("Meng", "Li"));
        customerRepository.save(new Customer("An", "Li"));
        customerRepository.save(new Customer("Mao", "Wang"));
        customerRepository.save(new Customer("Zheng", "Li"));
        List<Customer> resultList = customerRepository.findByFirstNameStartingWith("Me");
        LOGGER.info(resultList.toString());
        assertThat(resultList).hasSize(2);
    }

    @Test
    void findByFirstNameByDirectQuery_should_returnMatchingCustomers_whenFirstNameMatchesQuery() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Mei", "Li"));
        customerRepository.save(new Customer("Meng", "Li"));
        customerRepository.save(new Customer("An", "Li"));
        customerRepository.save(new Customer("Ming", "Wang"));
        customerRepository.save(new Customer("Zheng", "Li"));
        List<Customer> resultList = customerRepository.findByFirstNameByDirectQuery("Ming");
        // [Customer(id=1, firstName=Ming, lastName=Li), Customer(id=5, firstName=Ming, lastName=Wang)]
        LOGGER.info(resultList.toString());
        assertThat(resultList).hasSize(2);
    }

    @Test
    void findByFirstNameStartingWithByDirectQuery_should_returnMatchingCustomers_whenPrefixGiven() {
        customerRepository.save(new Customer("Ming", "Li"));
        customerRepository.save(new Customer("Mei", "Li"));
        customerRepository.save(new Customer("Meng", "Li"));
        customerRepository.save(new Customer("An", "Li"));
        customerRepository.save(new Customer("Mi", "Wang"));
        customerRepository.save(new Customer("Zheng", "Li"));
        List<Customer> resultList = customerRepository.findByFirstNameStartingWithByDirectQuery("Mi");
        // [Customer(id=1, firstName=Ming, lastName=Li), Customer(id=5, firstName=Mi, lastName=Wang)]
        LOGGER.info(resultList.toString());
        assertThat(resultList).hasSize(2);
    }
}
