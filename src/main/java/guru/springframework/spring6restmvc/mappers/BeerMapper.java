package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);    //mapstruct will provide the implementation (like lombok), you can see it in the compiled code

    BeerDTO beerToBeerDto(Beer beer);
}
