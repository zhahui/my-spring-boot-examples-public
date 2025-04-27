package net.huizha.examples.springboot.model.currency;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponse {

    private ExchangeResponseHead head;

    private ExchangeResponseData data;

    private List<ExchangeResponseRecord> records;
}
