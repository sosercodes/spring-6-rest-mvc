package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Optional<Customer> getCustomerById(UUID uuid);

    Customer saveNewCustomer(Customer customer);

    Customer updateCustomer(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);

    Customer patchCustomer(UUID customerId, Customer customer);
}
