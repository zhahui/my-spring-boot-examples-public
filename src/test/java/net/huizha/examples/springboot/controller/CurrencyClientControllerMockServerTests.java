package net.huizha.examples.springboot.controller;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.verify.VerificationTimes.exactly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;
import net.huizha.examples.springboot.TestConstants;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class CurrencyClientControllerMockServerTests {

    private static final ClientAndServer MOCK_SERVER = startClientAndServer(0);

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void logMockServer() {
        LOGGER.info("MockServer running on: {}", MOCK_SERVER.getPort());
    }

    @BeforeEach
    void resetExpectations() {
        MOCK_SERVER.reset();
    }

    @AfterAll
    static void stopServer() {
        MOCK_SERVER.stop();
    }

    @DynamicPropertySource
    static void registerBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("api.base-url", () -> "http://localhost:" + MOCK_SERVER.getLocalPort() + "/api/v1");
    }

    @Test
    void shouldReturnExchangeResponse() throws Exception {

        MOCK_SERVER
            .when(
                request()
                    .withMethod("GET")
                    .withPath(TestConstants.CURRENCY_SERVER_GET_CURRENCIES_PATH))
            .respond(
                response()
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "head": { "version": "2.0", "provider": "CWAP", "rep_code": "200" },
                          "data": {
                            "total": 2,
                            "startDate": "2025-03-01",
                            "searchlist": ["USD/CNY", "EUR/CNY"]
                          },
                          "records": [
                            { "date": "2025-04-09", "values": ["7.2066", "7.9620"] },
                            { "date": "2025-04-08", "values": ["7.2038", "7.9155"] }
                          ]
                        }
                        """));

        mockMvc.perform(get(TestConstants.CURRENCY_CLIENT_GET_CURRENCIES_PATH))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.head.version").value("2.0"))
               .andExpect(jsonPath("$.data.total").value(2))
               .andExpect(jsonPath("$.records[0].values[0]").value("7.2066"));

        MOCK_SERVER.verify(
            request().withMethod("GET").withPath(TestConstants.CURRENCY_SERVER_GET_CURRENCIES_PATH), exactly(1));
    }
}
