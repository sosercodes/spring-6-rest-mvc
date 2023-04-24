package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        Customer savedCustomer = customerService.updateCustomer(customerId, customer);
        return ResponseEntity.noContent()
                .eTag("v" + savedCustomer.getVersion())
                .lastModified(savedCustomer.getUpdateDate().atZone(ZoneId.systemDefault()))
                .build();
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        log.debug("CustomerController.handlePost()");
        Customer savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customers/" + savedCustomer.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public List<Customer> listCustomers() {
        log.debug("CustomerController.listCustomers()");
        return customerService.listCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController.getCustomerById(customerId:" + customerId + ")");
        return customerService.getCustomerById(customerId);
    }
}
