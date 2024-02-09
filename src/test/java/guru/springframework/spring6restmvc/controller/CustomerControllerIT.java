package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    @Rollback
    @Test
    void saveNewCustomer() {
        var customerDTO = CustomerDTO.builder().name("New Customer").build();

        var responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        var location = responseEntity.getHeaders().getLocation().toString();
        assertThat(location).startsWith(CustomerController.CUSTOMER_PATH);
        var uuid = UUID.fromString(location.split("/")[4]);
        assertThat(customerRepository.findById(uuid)).isNotNull();
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void getCustomerById() {
        var c = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(c.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testListCustomers() {
        var dtos = customerController.listCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Transactional
    @Rollback
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        var dtos = customerController.listCustomers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}