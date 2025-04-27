package net.huizha.examples.springboot.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.huizha.examples.springboot.model.currency.ExchangeResponse;

@RestController
@RequestMapping("/currency-server")
@Slf4j
@Tag(name = "Currency Controller", description = "Currency server and client controller")
public class CurrencyServerController {

    private static final String CURRENCY_JSON_FILE = "json/currency.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/currencies")
    public ExchangeResponse getCurrencyData() throws IOException {
        LOGGER.info("GET /currencies request received");
        try (InputStream inputStream = new ClassPathResource(CURRENCY_JSON_FILE).getInputStream()) {
            ExchangeResponse result = objectMapper.readValue(inputStream, ExchangeResponse.class);
            LOGGER.info("result={}", result);
            return result;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency data not found", e);
        }
    }
}
