package net.huizha.examples.springboot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.huizha.examples.springboot.model.customer.CustomerDto;
import net.huizha.examples.springboot.service.CustomerService;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customers Controller", description = "Create, Retrieve, Update and Delete customers")
public class CustomerController {

    private static final String MSG_CUSTOMER_NOT_FOUND = "Customer(id=%d) not found";

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Create a customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "New customer created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    public CustomerDto createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CustomerDto object to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDto.class)))
            @RequestBody
            @Valid CustomerDto customerDto) {
        LOGGER.info("POST /customers - customerDto={}", customerDto);
        return customerService.createCustomer(customerDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by id", description = "Get an existing customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Customer found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
    public CustomerDto getCustomerById(
            @Parameter(description = "id of customer to be retrieved") @PathVariable long id) {
        LOGGER.info("GET /customers/{} - id={}", id, id);
        Optional<CustomerDto> customerDto = customerService.getCustomerById(id);
        if (customerDto.isPresent()) {
            return customerDto.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(MSG_CUSTOMER_NOT_FOUND, id));
        }
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Get all customers")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",
            description = "Operation succeeded",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class))) }) })
    public List<CustomerDto> getCustomers() {
        LOGGER.info("GET /customers");
        return customerService.getCustomers();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer", description = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Customer updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
    public CustomerDto updateCustomerById(
            @Parameter(description = "id of customer to be updated") @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CustomerDto object to be updated",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDto.class)))
            @RequestBody
            @Valid CustomerDto customerDto) {
        LOGGER.info("PUT /customers - id={}; customerDto={}", id, customerDto);
        customerDto.setId(id);
        Optional<CustomerDto> updatedCustomer = customerService.updateCustomer(customerDto);
        if (updatedCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(MSG_CUSTOMER_NOT_FOUND, id));
        } else {
            return updatedCustomer.get();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a customer", description = "Delete an existing customer")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Customer deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
    public void deleteCustomerById(@Parameter(description = "id of customer to be deleted") @PathVariable long id) {
        LOGGER.info("DELETE /customers/{} - id={}", id, id);
        if (customerService.getCustomerById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(MSG_CUSTOMER_NOT_FOUND, id));
        } else {
            customerService.deleteCustomerById(id);
        }
    }
}
