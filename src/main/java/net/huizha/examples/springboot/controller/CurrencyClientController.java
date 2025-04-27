package net.huizha.examples.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.huizha.examples.springboot.model.currency.ExchangeResponse;

@RestController
@RequestMapping("/currency-client")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Currency Controller", description = "Currency server and client controller")
public class CurrencyClientController {

    private final RestTemplate restTemplate;

    // Base URL of the API, configurable via application properties
    @Value("${api.base-url:http://localhost:8080/api/v1}")
    private String baseUrl;

    @GetMapping("currencies")
    public ExchangeResponse getCurrencyData() {
        String url = baseUrl + "/currency-server/currencies";
        try {
            ExchangeResponse exchangeResponse = restTemplate.getForObject(url, ExchangeResponse.class);
            if (exchangeResponse != null) {
                LOGGER.info(exchangeResponse.toString());
                return exchangeResponse;
            } else {
                LOGGER.info("ExchangeResponse=null, return \"{}\"", HttpStatus.NO_CONTENT);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data returned from currency server");
            }
        } catch (HttpClientErrorException.NotFound notFoundEx) {
            LOGGER.warn("Currency server returned 404: {}", notFoundEx.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found on currency server");
        } catch (RestClientException rcEx) {
            LOGGER.error("Error during GET request to {}: {}", url, rcEx.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Currency client failed to fetch data");
        }
    }
}
