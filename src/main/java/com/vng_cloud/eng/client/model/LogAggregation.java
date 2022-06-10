package com.vng_cloud.eng.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAggregation {
    private LogAggregationQuery aggregationQuery;
    private LogAggregation subAggregation;
}
