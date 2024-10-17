package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";


    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<?> patchBeer(@PathVariable("beerId")UUID beerId, @RequestBody BeerDTO beer){
        if (beerService.patchBeerById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<?> deleteBeer(@PathVariable("beerId") UUID beerId) {
        if (!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<?> updateBeer(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) {
        beerService.updateBeerById(beerId, beer).orElseThrow(NotFoundException::new);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<?> handlePost(@Validated @RequestBody BeerDTO beer){
        var savedBeer = beerService.saveNewBeer(beer);
        return ResponseEntity.created(URI.create(BEER_PATH + "/" + savedBeer.getId().toString())).build();
    }

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle) {
        return beerService.listBeers(beerName, beerStyle);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController.getBeerById(uuid:" + beerId + ") - 123");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
