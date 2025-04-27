package net.huizha.examples.springboot.model.currency;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponseData {

    private int total;

    private String startDate;

    private int pageTotal;

    @JsonProperty("searchlist")
    private List<String> searchList;

    private int pageSize;

    private String endDate;

    private String flagMessage;

    private List<String> head;

    private String currency;

    private int pageNum;
}
