package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(uuid).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<CustomerDTO> atomicReference= new AtomicReference<CustomerDTO>();
        customerRepository.findById(customerId).ifPresent(c -> {
            c.setName(customer.getName());
            Customer savedCustomer = customerRepository.save(c);
            atomicReference.set(customerMapper.customerToCustomerDto(savedCustomer));
        });
        return Optional.ofNullable(atomicReference.get());
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<CustomerDTO> atomicReference= new AtomicReference<CustomerDTO>();
        customerRepository.findById(customerId).ifPresent(c -> {
            if (StringUtils.hasText(customer.getName())) {
                c.setName(customer.getName());
            }
            var savedCustomer = customerRepository.save(c);
            atomicReference.set(customerMapper.customerToCustomerDto(savedCustomer));
        });
        return Optional.ofNullable(atomicReference.get());
    }
}
