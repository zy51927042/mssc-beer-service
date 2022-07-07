package guru.springframework.msscbeerservice.services;


import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.print.DocFlavor;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerUpcCache",key = "#upc")
    @Override
    public BeerDto getByUpc(String upc) {
        System.out.println("I was called");
        return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc).orElseThrow(NotFoundException::new));
    }

    @Cacheable(cacheNames = "beerCache",key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        System.out.println("I was called");
        if(showInventoryOnHand){
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        }else{
            return beerMapper.beerToBeerDto(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        }

    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(
                beerRepository.save(beerMapper.beerDtoToBeer(beerDto))
        );
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));

    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle,
                                   PageRequest pageRequest,Boolean showInventoryOnHand) {

        System.out.println("I was called");
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;
        if(StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName,beerStyle,pageRequest);
        }else if(StringUtils.hasText(beerName) && beerStyle==null){
            beerPage = beerRepository.findAllByBeerName(beerName,pageRequest);
        }else if(!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle,pageRequest);
        }else{
            beerPage = beerRepository.findAll(pageRequest);
        }
        if(showInventoryOnHand){
            beerPagedList = new BeerPagedList(beerPage.getContent().stream()
                    .map(beerMapper::beerToBeerDtoWithInventory).collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),beerPage.getTotalElements());
        }else{
            beerPagedList = new BeerPagedList(beerPage.getContent().stream()
                    .map(beerMapper::beerToBeerDto).collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),beerPage.getTotalElements());
        }

        return beerPagedList;

    }
}
