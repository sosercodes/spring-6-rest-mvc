package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.TestcontainersConfiguration;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@ActiveProfiles("localmysql")
@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testPatchCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.patchCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Transactional
    @Rollback
    @Test
    void testPatchCustomerById() {
        var customer = customerRepository.findAll().get(0);
        var customerDto = customerMapper.customerToCustomerDto(customer);
        customerDto.setName("New Customer");

        ResponseEntity responseEntity = customerController.patchCustomer(customer.getId(), customerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId()).get().getName()).isEqualTo(customerDto.getName());
    }

    @Test
    void testDeleteCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomer(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void testCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteCustomer(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }


    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateCustomer() {
        var customer = customerRepository.findAll().get(0);
        var customerDto = customerMapper.customerToCustomerDto(customer);
        customerDto.setName("New Customer");
        customer.setVersion(0);

        ResponseEntity responseEntity = customerController.updateCustomer(customer.getId(), customerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId()).get().getName()).isEqualTo(customerDto.getName());
    }

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