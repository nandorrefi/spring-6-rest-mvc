package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest    // test slice for Spring Data JPA components
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");        // sql wildcards

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testGetBeerListByStyle() {
        List<Beer> list = beerRepository.findAllByBeerStyle(BeerStyle.IPA);

        assertThat(list.size()).isEqualTo(548);
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(
                    Beer.builder()
                            .beerName("My Beer 123451123451123451123451123451123451123451123451123451123451123451123451123451123451123451123451123451123451")
                            .beerStyle(BeerStyle.LAGER)
                            .upc("asd")
                            .price(new BigDecimal("324.34"))
                            .build()
            );

            beerRepository.flush(); // makes hibernate to immediately write to the DB, otherwise validation does not run for the test because the test ends too quickly
        });

    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(
                Beer.builder()
                        .beerName("My Beer")
                        .beerStyle(BeerStyle.LAGER)
                        .upc("asd")
                        .price(new BigDecimal("324.34"))
                        .build()
        );

        beerRepository.flush(); // makes hibernate to immediately write to the DB, otherwise validation does not run for the test because the test ends too quickly

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

}