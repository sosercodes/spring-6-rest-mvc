package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeerServiceImplTest {

    @Test
    void updateBeerByIdNotFound() {
        BeerServiceImpl beerService = new BeerServiceImpl();
        assertEquals(beerService.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build()), Optional.empty());
    }
}