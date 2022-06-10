package com.vng_cloud.eng.client.service;

import com.vng_cloud.eng.client.request.ExportLogRequest;
import com.vng_cloud.eng.client.request.SearchLogRequest;
import com.vng_cloud.eng.client.response.ExportLogResponse;

public interface LogService {
    String searchLogs(SearchLogRequest searchLogRequest);
    ExportLogResponse createExportProcess(ExportLogRequest exportLogRequest);
}
