package net.huizha.examples.springboot.model.currency;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponseRecord {

    private String date;

    private List<String> values;
}
