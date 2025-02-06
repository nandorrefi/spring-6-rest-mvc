package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // so that bootstrap loads some test data for us
class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .build();

        //BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder); // save object and persist to the database then return object from database
        // with flushing (persisting to database) JPA can retrieve the relationship between objects, but constant flushing can lead to performance degradation

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder); // With lazy loading the relationships of this object won't be resolved immediately
        // so currently we won't be able to access savedBeerOrder.customer
        // if we want to init the relations without flushing, then we need to set up our own helper methods to build the relationship ourselves (see setCustomer method in BeerOrder)

        System.out.println(savedBeerOrder.getCustomerRef());
    }
}