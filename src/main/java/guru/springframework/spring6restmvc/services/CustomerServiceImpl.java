package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        log.debug("CustomerServiceImpl.getCustomerById(uuid:" + uuid + ")");
        return Optional.ofNullable(customerMap.get(uuid));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        log.debug("CustomerServiceImpl.saveNewCustomer(" + customer.getName() + ")");
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        customerMap.put(savedCustomer.getId(), savedCustomer);
        log.debug("CustomerServiceImpl.saveNewCustomer -> created new customer with id:" + savedCustomer.getId());
        return savedCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
        if (existingCustomer != null) {
            existingCustomer.setName(customer.getName());
            Integer version = existingCustomer.getVersion();
            version++;
            existingCustomer.setVersion(version);
            existingCustomer.setUpdateDate(LocalDateTime.now());
        }

        return Optional.ofNullable(existingCustomer);
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        return customerMap.remove(customerId) != null;
    }

    @Override
    public CustomerDTO patchCustomer(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
        if (StringUtils.hasText(customer.getName())) {
            existingCustomer.setName(customer.getName());
        }
        Integer version = existingCustomer.getVersion();
        version++;
        existingCustomer.setVersion(version);
        existingCustomer.setUpdateDate(LocalDateTime.now());
        customerMap.put(existingCustomer.getId(), existingCustomer);
        return existingCustomer;
    }
}
