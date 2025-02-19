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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    public static final int MAX_PAGE_SIZE = 1000;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if(StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory == null || !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = pageNumber != null && pageNumber > 0 ? pageNumber - 1 : DEFAULT_PAGE;
        int queryPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        queryPageSize = Math.min(queryPageSize, MAX_PAGE_SIZE);

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> listBeersByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(
                beerMapper.beerToBeerDto(
                        beerRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(
                beerRepository.save(
                        beerMapper.beerDtoToBeer(beer)
                )
        );
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        
        // this return will fail the "testUpdateBeerBadVersion" test because the beerDtoToBeer function
        // creates a detached Beer object that works outside of a transactional context, when hibernate tries to save this object,
        // it finds that the version is different on the optimistic locking value so it throws an ObjectOptimisticLockingFailureException.

        // return Optional.of(beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer))));

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {  // "testUpdateBeerBadVersion" test will not fail due to optimistic locking
            // because findById creates an implicit transaction with an attached beerDTO object and updates this object in the same transaction.
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            foundBeer.setVersion(beer.getVersion());
            atomicReference.set(
                    Optional.of(
                            beerMapper.beerToBeerDto(beerRepository.save(foundBeer))
                    )
            );
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }

        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if(StringUtils.hasText(beer.getBeerName())) {
                foundBeer.setBeerName(beer.getBeerName());
            }

            if(beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }

            if(StringUtils.hasText(beer.getUpc())) {
                foundBeer.setUpc(beer.getUpc());
            }

            if(beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }

            if(beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }

            atomicReference.set(
                    Optional.of(
                            beerMapper.beerToBeerDto(beerRepository.save(foundBeer))
                    )
            );
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
