package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class PricingMicroserviceTest {

    @Test
    public void getAllPricesTest() {
        ResponseEntity<Map> responseEntity = new TestRestTemplate().getForEntity("http" +
                        "://localhost:8082" +
                        "/prices",
                Map.class);
        Map map = (LinkedHashMap) responseEntity.getBody().get("_embedded");
        List prices = (List) map.get("prices");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

//    @Test
//    public void getPriceWithExistingIDTest() {
//        ResponseEntity<Price> responseEntity = new TestRestTemplate().getForEntity("http" +
//                        "://localhost:8082" +
//                        "/prices/1",
//                Price.class);
//        Price price = responseEntity.getBody();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(price.getPrice().longValue(), 1000);
//        assertEquals(price.getCurrency(), "USD");
//    }

    @Test
    public void getPriceWithInvalidIDTest() {
        ResponseEntity<Price> responseEntity = new TestRestTemplate().getForEntity("http" +
                        "://localhost:8082" +
                        "/prices/00",
                Price.class);
        Price price = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void addVehiclePriceTest() {
        Price price = new Price("RS",new BigDecimal(5000));
        ResponseEntity<Price> responseEntity = new TestRestTemplate().postForEntity("http" +
                "://localhost:8082/prices", price, Price.class);
        assertEquals(HttpStatus.CREATED ,responseEntity.getStatusCode());
    }

    @Test
    public void deleteVehiclePriceTest() {
        new TestRestTemplate().delete("http" +
                "://localhost:8082/prices/1");
        ResponseEntity<Price> responseEntity = getPriceById(1L);

    }

    private ResponseEntity<Price> getPriceById(Long id) {
        return new TestRestTemplate().getForEntity("http" +
                        "://localhost:8082" +
                        "/prices/"+id,
                Price.class);
    }
}
