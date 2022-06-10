package com.vng_cloud.eng.client.request;

import com.vng_cloud.eng.client.model.LogAggregation;
import com.vng_cloud.eng.client.model.LogQuery;
import com.vng_cloud.eng.client.model.LogSort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchLogRequest {
    String projectId;
    List<LogSort> sorts;
    Integer size;
    Integer from;
    LogQuery query;
    List<LogAggregation> aggregations;
}
