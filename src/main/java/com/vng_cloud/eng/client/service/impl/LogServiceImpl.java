package com.vng_cloud.eng.client.service.impl;

import com.vng_cloud.eng.client.request.ExportLogRequest;
import com.vng_cloud.eng.client.request.SearchLogRequest;
import com.vng_cloud.eng.client.response.ExportLogResponse;
import com.vng_cloud.eng.client.config.ClientConfig;
import com.vng_cloud.eng.client.exception.HttpHandledException;
import com.vng_cloud.eng.client.service.LogService;
import okhttp3.OkHttpClient;

public class LogServiceImpl extends BaseService implements LogService {
    public LogServiceImpl(OkHttpClient client, ClientConfig vmonitorLogClientConfig) {
        super(client, vmonitorLogClientConfig);
    }

    @Override
    public String searchLogs(SearchLogRequest searchLogRequest)throws HttpHandledException {
        return post(String.format("%s/v1/logs/search", clientConfig.getBaseUrl()), searchLogRequest,String.class);
    }

    @Override
    public ExportLogResponse createExportProcess(ExportLogRequest exportLogRequest)throws HttpHandledException{
        return post(String.format("%s/v1/logs/exports", clientConfig.getBaseUrl()), exportLogRequest,ExportLogResponse.class);
    }

}
