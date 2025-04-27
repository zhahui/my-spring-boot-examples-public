package net.huizha.examples.springboot.model.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "version",
    "provider",
    "req_code",
    "rep_code",
    "rep_message",
    "ts",
    "producer",
    "tstext"
})
public class ExchangeResponseHead {

    private String version;

    private String provider;

    @JsonProperty("req_code")
    private String reqCode;

    @JsonProperty("rep_code")
    private String repCode;

    @JsonProperty("rep_message")
    private String repMessage;

    @JsonProperty("ts")
    private long timestamp;

    private String producer;

    @JsonProperty("tstext")
    private String timestampText;
}
