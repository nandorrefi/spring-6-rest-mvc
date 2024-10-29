package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{id}";

    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchByid(@PathVariable("id") UUID customerId, @RequestBody Customer customer) {
        customerService.patchById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("id") UUID customerId ) {

        customerService.deleteById(customerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("id") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        return ResponseEntity.created(URI.create(CUSTOMER_PATH + "/" + savedCustomer.getId())).build(); // basically the same thing we did in BeerController POST
    }

    @GetMapping(CUSTOMER_PATH)
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable("id") UUID customerId) {
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }
}
