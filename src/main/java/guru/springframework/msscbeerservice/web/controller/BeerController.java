package guru.springframework.msscbeerservice.web.controller;

import com.sun.istack.NotNull;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/beer")
@RestController
@Validated
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@NotNull @PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerService.getById(beerId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDto beerDto){
        return new ResponseEntity<>(beerService.saveNewBeer(beerDto),HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId,@RequestBody @Validated BeerDto beerDto){
        return new ResponseEntity<>(beerService.updateBeer(beerId, beerDto),HttpStatus.NO_CONTENT);
    }
}
