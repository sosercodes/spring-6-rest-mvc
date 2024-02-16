package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customers";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}" ;

    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        var patchedCustomer = customerService.patchCustomer(customerId, customer);
        if (patchedCustomer.isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerId) {
        if (! customerService.deleteCustomerById(customerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.updateCustomer(customerId, customer).orElseThrow(NotFoundException::new);
        return ResponseEntity.noContent()
                .eTag("v" + savedCustomer.getVersion())
                .lastModified(savedCustomer.getUpdateDate().atZone(ZoneId.systemDefault()))
                .build();
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody CustomerDTO customer) {
        log.debug("CustomerController.handlePost()");
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers() {
        log.debug("CustomerController.listCustomers()");
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController.getCustomerById(customerId:" + customerId + ")");
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }
}
