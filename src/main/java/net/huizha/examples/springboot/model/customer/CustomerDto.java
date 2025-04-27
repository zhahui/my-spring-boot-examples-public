package net.huizha.examples.springboot.model.customer;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
