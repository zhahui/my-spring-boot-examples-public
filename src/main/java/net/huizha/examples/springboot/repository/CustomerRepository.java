package net.huizha.examples.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import net.huizha.examples.springboot.model.customer.Customer;

@Repository
@Validated
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFirstName(@NotBlank String firstName);

    List<Customer> findByLastNameOrderByFirstNameAsc(@NotBlank String lastName);

    List<Customer> findByFirstNameStartingWith(@NotBlank String prefix);

    @Query(value = "SELECT c from Customer c WHERE c.firstName = :firstName")
    List<Customer> findByFirstNameByDirectQuery(@Param("firstName") @NotBlank String firstName);

    @Query(value = "SELECT c from Customer c WHERE c.firstName LIKE :prefix%")
    List<Customer> findByFirstNameStartingWithByDirectQuery(@Param("prefix") @NotBlank String prefix);
}
