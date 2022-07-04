package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.UUID;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validDto;

    @BeforeEach
    void setUp() {
        validDto = BeerDto.builder()
                .beerName("My beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("12.3"))
                .upc(12324445L)
                .build();
    }

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/beer/{beerId}" , UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                          parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer"),
                                fieldWithPath("version").description("version"),
                                fieldWithPath("createdDate").description("createdDate"),
                                fieldWithPath("lastModifiedDate").description("lastModifiedDate"),
                                fieldWithPath("beerName").description("Name of Beer"),
                                fieldWithPath("beerStyle").description("Style of Beer"),
                                fieldWithPath("upc").description("upc of Beer"),
                                fieldWithPath("price").description("price of Beer"),
                                fieldWithPath("quantityOnHand").description("quantityOnHand")

                        )
                ));
    }

    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDto = validDto;
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/beer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer-new",
                        requestFields(
                                fieldWithPath("id").ignored(),
                                fieldWithPath("version").ignored(),
                                fieldWithPath("createdDate").ignored(),
                                fieldWithPath("lastModifiedDate").ignored(),
                                fieldWithPath("beerName").description("Name of the beer"),
                                fieldWithPath("beerStyle").description("Style of Beer"),
                                fieldWithPath("upc").description("upc of Beer"),
                                fieldWithPath("price").description("price of Beer"),
                                fieldWithPath("quantityOnHand").ignored()
                        )));

    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = validDto;
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/beer/" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }
}