package com.vng_cloud.eng.client.request;

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
public class ExportLogRequest {
    String projectId;
    List<LogSort> sorts;
    LogQuery query;
}
