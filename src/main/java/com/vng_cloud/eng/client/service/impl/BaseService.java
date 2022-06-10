package com.vng_cloud.eng.client.service.impl;

import com.vng_cloud.eng.client.VngCloudClientHelper;
import com.vng_cloud.eng.client.config.ClientConfig;
import com.vng_cloud.eng.client.constant.Method;
import com.vng_cloud.eng.client.exception.HttpHandledException;
import com.vng_cloud.eng.client.model.IamToken;
import com.google.gson.Gson;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class BaseService {
    private OkHttpClient client;
    protected ClientConfig clientConfig;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Gson gson = new Gson();

    protected Logger logger = LogManager.getLogger(BaseService.class);

    public BaseService(OkHttpClient okHttpClient, ClientConfig clientConfig) {
        this.client = okHttpClient;
        this.clientConfig = clientConfig;
    }

    private String execute(Request request) throws HttpHandledException {
        try {
            logger.info("base-service<--- " + gson.toJson(request));
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = Optional.ofNullable(response.body())
                        .orElseThrow(() -> new HttpHandledException("Not found body response", response.code())).string();
                logger.info("base-service---> " + json);
                return json;
            } else {
                logger.info("base-service---> " + response.message());
                logger.error(response.message());
                throw new HttpHandledException(response.message(), response.code());
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
            throw new HttpHandledException(ioException.getMessage(), 500);
        }
    }

    protected <T> T get(String url, Class<T> clazz) throws HttpHandledException {
        String jsonResponse = execute(buildRequest(url, Method.GET, null));
        if (clazz == String.class) {
            return clazz.cast(jsonResponse);
        }
        return gson.fromJson(jsonResponse, clazz);
    }

    protected <T> T post(String url, Object body, Class<T> clazz) throws HttpHandledException {
        String jsonResponse = execute(buildRequest(url, Method.POST, body));
        if (clazz == String.class) {
            return clazz.cast(jsonResponse);
        }
        return gson.fromJson(jsonResponse, clazz);
    }

    protected <T> T put(String url, Object body, Class<T> clazz) throws HttpHandledException {
        String jsonResponse = execute(buildRequest(url, Method.PUT, body));
        if (clazz == String.class) {
            return clazz.cast(jsonResponse);
        }
        return gson.fromJson(jsonResponse, clazz);
    }

    protected <T> T delete(String url, Object body, Class<T> clazz) throws HttpHandledException {
        String jsonResponse = execute(buildRequest(url, Method.DELETE, body));
        if (clazz == String.class) {
            return clazz.cast(jsonResponse);
        }
        return gson.fromJson(jsonResponse, clazz);
    }

    private Request buildRequest(String url, Method method, Object body) {
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON);
        Request request = new Request.Builder()
                .url(url)
                .build();
        switch (method) {
            case GET:
                request = request.newBuilder().get().build();
                break;
            case POST:
                request = request.newBuilder().post(requestBody).build();
                break;
            case PUT:
                request = request.newBuilder().put(requestBody).build();
                break;
            case DELETE:
                request = request.newBuilder().delete(requestBody).build();
                break;
        }
        try {
            long timeStamp = System.currentTimeMillis() / 1000;
            IamToken iamToken = VngCloudClientHelper.getTokenService().getToken();
            request = request.newBuilder()
                    .addHeader("timestamp", Long.toString(timeStamp))
                    .addHeader("Authorization", String.format("%s %s", iamToken.getTokenType(), iamToken.getAccessToken()))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HttpHandledException("Internal error", 500);
        }
        return request;
    }
}
