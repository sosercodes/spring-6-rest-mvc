package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import({TestcontainersConfiguration.class, BootstrapData.class, BeerCsvServiceImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        properties = {
                "spring.datasource.url = jdbc:tc:mysql:8.0://hostname/test-database",
                "spring.datasource.driver-class-name = org.testcontainers.jdbc.ContainerDatabaseDriver"
        }
)
@DataJpaTest
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
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Test
    void testBeerOrders() {
        System.out.println(beerOrderRepository.count());
        System.out.println(customerRepository.count());
        System.out.println(beerRepository.count());
        System.out.println(testCustomer.getName());
        System.out.println(testBeer.getBeerName());
    }
}