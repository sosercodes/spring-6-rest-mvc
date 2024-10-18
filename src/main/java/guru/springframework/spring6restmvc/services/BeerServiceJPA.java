package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        final Pageable pageRequest = buildPageRequest(pageNumber, pageSize);
        final Page<Beer> page;
        if (StringUtils.hasText(beerName) && (beerStyle == null)) {
            page = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            page = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            page = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            page = beerRepository.findAll(pageRequest);
        }
        if (showInventory != null && !showInventory) {
            page.forEach(b -> b.setQuantityOnHand(null));
        }
        return page.map(beerMapper::beerToBeerDto);
    }

    public Pageable buildPageRequest(Integer pageNumber, Integer pageSize) {
        final int page = pageNumber == null ? DEFAULT_PAGE : pageNumber - 1;
        final int size = pageSize == null ? DEFAULT_PAGE_SIZE : Math.min(pageSize, 1000);
        final Sort sort = Sort.by(Sort.Direction.ASC, "beerName");
        return PageRequest.of(page, size, sort);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    private Page<Beer> listBeersByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
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
