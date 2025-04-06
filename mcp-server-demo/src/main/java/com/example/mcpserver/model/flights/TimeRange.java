package com.example.mcpserver.model.flights;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeRange {
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;
}
