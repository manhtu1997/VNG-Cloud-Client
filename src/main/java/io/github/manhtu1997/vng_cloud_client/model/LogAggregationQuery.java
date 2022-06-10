package io.github.manhtu1997.vng_cloud_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LogAggregationQuery {
    String type;
    Object value;
}
