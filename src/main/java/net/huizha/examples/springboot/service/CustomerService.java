package net.huizha.examples.springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.huizha.examples.springboot.model.customer.Customer;
import net.huizha.examples.springboot.model.customer.CustomerDto;
import net.huizha.examples.springboot.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
@Validated
public class CustomerService {

    private final CustomerRepository customerRepository;

    private CustomerDto toCustomerDtoFrom(@Valid @NotNull Customer customer) {
        return CustomerDto.builder().id(customer.getId()).firstName(customer.getFirstName())
                .lastName(customer.getLastName()).build();
    }

    private Customer toCustomerFrom(@Valid @NotNull CustomerDto customerDto) {
        return new Customer(customerDto.getFirstName(), customerDto.getLastName());
    }

    public CustomerDto createCustomer(@Valid @NotNull CustomerDto customerDto) {
        Customer createdCustomer = customerRepository.save(toCustomerFrom(customerDto));
        return toCustomerDtoFrom(createdCustomer);
    }

    public List<CustomerDto> getCustomers() {
        List<CustomerDto> result = new ArrayList<>();
        List<Customer> customerList = customerRepository.findAll();
        for (Customer customer : customerList) {
            result.add(toCustomerDtoFrom(customer));
        }
        return result;
    }

    public Optional<CustomerDto> getCustomerById(long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            return Optional.of(toCustomerDtoFrom(customerOpt.get()));
        } else {
            return Optional.empty();
        }
    }

    public Optional<CustomerDto> updateCustomer(@Valid @NotNull CustomerDto customerDto) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(customerDto.getId());
        if (existingCustomerOpt.isEmpty()) {
            return Optional.empty();
        } else {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setFirstName(customerDto.getFirstName());
            existingCustomer.setLastName(customerDto.getLastName());
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return Optional.of(toCustomerDtoFrom(updatedCustomer));
        }
    }

    public void deleteCustomerById(long id) {
        customerRepository.deleteById(id);
    }
}
