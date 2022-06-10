package io.github.manhtu1997.vng_cloud_client.request;

import io.github.manhtu1997.vng_cloud_client.model.LogQuery;
import io.github.manhtu1997.vng_cloud_client.model.LogSort;
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
