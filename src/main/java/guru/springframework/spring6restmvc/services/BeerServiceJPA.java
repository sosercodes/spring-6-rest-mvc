package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
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
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        final List<Beer> beerList;
        if (StringUtils.hasText(beerName) && (beerStyle == null)) {
            beerList = listBeersByName(beerName);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = listBeersByStyle(beerStyle);
        } else {
            beerList = beerRepository.findAll();
        }
        return beerList.stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    private List<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle);
    }

    private List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID uuid) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(uuid).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO) {
        final AtomicReference<BeerDTO> atomicReference = new AtomicReference<BeerDTO>();
        beerRepository.findById(beerId).ifPresent(b -> {
                    b.setBeerName(beerDTO.getBeerName());
                    b.setBeerStyle(beerDTO.getBeerStyle());
                    b.setUpc(beerDTO.getUpc());
                    b.setPrice(beerDTO.getPrice());
                    b.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    atomicReference.set(beerMapper.beerToBeerDto(beerRepository.save(b)));
                }
        );
        return Optional.ofNullable(atomicReference.get());
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        var beerFound = beerRepository.findById(beerId);
        AtomicReference<BeerDTO> patchedBeer = new AtomicReference<>();
        beerFound.ifPresent(b -> {
            if (StringUtils.hasText(beer.getBeerName())){
                b.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                b.setBeerStyle(beer.getBeerStyle());
            }
            if (beer.getPrice() != null) {
                b.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null){
                b.setQuantityOnHand(beer.getQuantityOnHand());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                b.setUpc(beer.getUpc());
            }
            beerRepository.save(b);
            patchedBeer.set(beerMapper.beerToBeerDto(b));
        });
        return Optional.ofNullable(patchedBeer.get());
    }
}
