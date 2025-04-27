package net.huizha.examples.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import jakarta.validation.ConstraintViolationException;
import net.huizha.examples.springboot.model.customer.CustomerDto;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerServiceTests {

    @Autowired
    private CustomerService customerService;

    private CustomerDto customerDto1;

    private CustomerDto customerDto2;

    private CustomerDto createdCustomerDto1;

    private CustomerDto createdCustomerDto2;

    @BeforeEach
    void setUpBeforeEachTest(TestInfo testInfo) {
        if (testInfo.getDisplayName().startsWith("createCustomer")) {
            return;
        }
        customerDto1 = CustomerDto.builder().firstName("Ming").lastName("Li").build();
        createdCustomerDto1 = customerService.createCustomer(customerDto1);
        customerDto1.setId(1L);
        assertThat(customerDto1).isEqualTo(createdCustomerDto1);
        customerDto2 = CustomerDto.builder().firstName("Hong").lastName("Wang").build();
        createdCustomerDto1 = customerService.createCustomer(customerDto2);
        customerDto2.setId(2L);
        assertThat(customerDto2).isEqualTo(createdCustomerDto1);
    }

    @Test
    void createCustomer_should_throwConstraintViolation_whenCustomerIsNull() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            customerService.createCustomer(null);
        }).withMessageMatching("createCustomer.customerDto: must not be null");
    }

    @Test
    void createCustomer_should_throwConstraintViolation_whenFirstNameIsNull() {
        CustomerDto customerDto = CustomerDto.builder().lastName("Li").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto))
            .withMessageMatching("createCustomer.customerDto.firstName: must not be blank");
    }

    @Test
    void createCustomer_should_throwConstraintViolation_whenFirstNameIsBlank() {
        customerDto1 = CustomerDto.builder().firstName("").lastName("Li").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto1))
            .withMessageContaining("createCustomer.customerDto.firstName: must not be blank");
        customerDto2 = CustomerDto.builder().firstName(" ").lastName("Li").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto2))
            .withMessageContaining("createCustomer.customerDto.firstName: must not be blank");
    }

    @Test
    void createCustomer_should_throwConstraintViolation_whenLastNameIsNull() {
        CustomerDto customerDto = CustomerDto.builder().firstName("Ming").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto))
            .withMessageMatching("createCustomer.customerDto.lastName: must not be blank");
    }

    @Test
    void createCustomer_should_throwConstraintViolation_whenLastNameIsBlank() {
        customerDto1 = CustomerDto.builder().firstName("Ming").lastName("").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto1))
            .withMessageContaining("createCustomer.customerDto.lastName: must not be blank");
        customerDto2 = CustomerDto.builder().firstName("Ming").lastName(" ").build();
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> customerService.createCustomer(customerDto2))
            .withMessageContaining("createCustomer.customerDto.lastName: must not be blank");
    }

    @Test
    void createCustomer_should_createCustomer_whenInputIsValid() {
        CustomerDto customerDto = CustomerDto.builder().firstName("Ming").lastName("Li").build();
        CustomerDto actual = customerService.createCustomer(customerDto);
        customerDto.setId(1L);
        assertThat(actual).isEqualTo(customerDto);
    }

    @Test
    void getCustomers_should_returnAllCustomers_whenCalled() {
        List<CustomerDto> expected = new ArrayList<>();
        expected.add(createdCustomerDto1);
        expected.add(createdCustomerDto2);
        assertThat(customerService.getCustomers()).hasSize(2).isEqualTo(customerService.getCustomers());
    }

    @Test
    void getCustomerById_should_returnEmpty_whenIdNotExist() {
        assertThat(customerService.getCustomerById(3L)).isEmpty();
    }

    @Test
    void getCustomerById_should_returnCustomer_whenIdExists() {
        assertThat(customerDto1).isEqualTo(customerService.getCustomerById(1L).get());
        assertThat(customerDto2).isEqualTo(customerService.getCustomerById(2L).get());
    }

    @Test
    void updateCustomer_should_updateCustomer_whenInputIsValid() {
        customerDto2.setFirstName("John");
        Optional<CustomerDto> updatedCustomerDto = customerService.updateCustomer(customerDto2);
        assertThat(updatedCustomerDto).isPresent();
        assertThat(updatedCustomerDto.get().getFirstName()).isEqualTo("John");
        assertThat(updatedCustomerDto.get().getLastName()).isEqualTo("Wang");
    }

    @Test
    void updateCustomer_should_throwConstraintViolation_whenCustomerIsNull() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            customerService.updateCustomer(null);
        }).withMessageMatching("updateCustomer.customerDto: must not be null");
    }

    @Test
    void updateCustomer_should_returnEmpty_whenIdNotExist() {
        customerDto2.setId(3L);
        assertThat(customerService.updateCustomer(customerDto2)).isEmpty();
    }

    @Test
    void deleteCustomerById_should_deleteCustomer_whenIdExists() {
        assertThat(customerService.getCustomerById(2L)).isPresent();
        customerService.deleteCustomerById(2L);
        assertThat(customerService.getCustomerById(2L)).isEmpty();
    }

    @Test
    void deleteCustomerById_should_notThrowEmptyResultDataAccessException_whenIdNotExist() {
        assertThat(customerService.getCustomerById(3L)).isEmpty();
        assertThat(customerService.getCustomers()).isInstanceOf(List.class).hasSize(2);
        assertDoesNotThrow(() -> {
            customerService.deleteCustomerById(3L);
        });
        assertThat(customerService.getCustomers()).isInstanceOf(List.class).hasSize(2);
    }
}
