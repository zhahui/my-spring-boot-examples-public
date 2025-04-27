package net.huizha.examples.springboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import net.huizha.examples.springboot.model.customer.CustomerDto;
import net.huizha.examples.springboot.util.JsonUtil;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postCustomers_should_createCustomer_whenRequestIsValid() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.asJsonString(CustomerDto.builder().firstName("Ming").lastName("Li").build())))
                .andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ming")).andExpect(jsonPath("$.lastName").value("Li"));
    }

    @Test
    void getCustomerById_should_returnCustomer_whenCustomerExists() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.asJsonString(CustomerDto.builder().firstName("Ming").lastName("Li").build())))
                .andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ming")).andExpect(jsonPath("$.lastName").value("Li"));
        mockMvc.perform(get("/customers/1").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ming")).andExpect(jsonPath("$.lastName").value("Li"));
    }
}
