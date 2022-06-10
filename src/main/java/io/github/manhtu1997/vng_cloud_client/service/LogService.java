package io.github.manhtu1997.vng_cloud_client.service;

import io.github.manhtu1997.vng_cloud_client.request.ExportLogRequest;
import io.github.manhtu1997.vng_cloud_client.request.SearchLogRequest;
import io.github.manhtu1997.vng_cloud_client.response.ExportLogResponse;

public interface LogService {
    String searchLogs(SearchLogRequest searchLogRequest);
    ExportLogResponse createExportProcess(ExportLogRequest exportLogRequest);
}
