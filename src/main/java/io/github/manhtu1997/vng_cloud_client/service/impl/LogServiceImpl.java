package io.github.manhtu1997.vng_cloud_client.service.impl;

import io.github.manhtu1997.vng_cloud_client.config.ClientConfig;
import io.github.manhtu1997.vng_cloud_client.exception.HttpHandledException;
import io.github.manhtu1997.vng_cloud_client.request.ExportLogRequest;
import io.github.manhtu1997.vng_cloud_client.request.SearchLogRequest;
import io.github.manhtu1997.vng_cloud_client.response.ExportLogResponse;
import io.github.manhtu1997.vng_cloud_client.service.LogService;
import okhttp3.OkHttpClient;

public class LogServiceImpl extends BaseService implements LogService {
    public LogServiceImpl(OkHttpClient client, ClientConfig vmonitorLogClientConfig) {
        super(client, vmonitorLogClientConfig);
    }

    @Override
    public String searchLogs(SearchLogRequest searchLogRequest)throws HttpHandledException {
        return post(String.format("%s/v1/projects/%s/search-logs", clientConfig.getBaseUrl(),searchLogRequest.getProjectId())
                , searchLogRequest,String.class);
    }

    @Override
    public ExportLogResponse createExportProcess(ExportLogRequest exportLogRequest)throws HttpHandledException{
        return post(String.format("%s/v1/logs/exports", clientConfig.getBaseUrl()), exportLogRequest,ExportLogResponse.class);
    }

}
