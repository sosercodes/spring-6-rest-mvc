package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    @GetMapping("/api/v1/beer")
    public List listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "/api/v1/beer/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController.getBeerById(uuid:" + beerId + ")");
        return beerService.getBeerById(beerId);
    }
}
