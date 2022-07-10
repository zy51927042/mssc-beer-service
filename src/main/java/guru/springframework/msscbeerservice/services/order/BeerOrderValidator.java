package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeerOrderValidator {
    private final BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrderDto) {
        AtomicInteger beersNotFound = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(orderLine ->{
            if(beerRepository.findByUpc(orderLine.getUpc()) == null) {
                beersNotFound.incrementAndGet();
            }
        });
        return beersNotFound.get() == 0;
    }

}
