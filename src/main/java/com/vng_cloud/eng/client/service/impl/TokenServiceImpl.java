package com.vng_cloud.eng.client.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vng_cloud.eng.client.config.IamClientConfig;
import com.vng_cloud.eng.client.constant.VngCloudClientConstant;
import com.vng_cloud.eng.client.service.TokenService;
import com.vng_cloud.eng.client.constant.RestHeaderConstants;
import com.vng_cloud.eng.client.model.IamToken;
import com.vng_cloud.eng.client.request.ExchangeTokenRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class TokenServiceImpl implements TokenService {
    public TokenServiceImpl(OkHttpClient client, IamClientConfig iamClientConfig) {
        this.client = client;
        this.iamClientConfig = iamClientConfig;
    }
    private OkHttpClient client;

    private IamClientConfig iamClientConfig;
    private static IamToken token;
    private static Date tokenExp;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Logger logger = LogManager.getLogger(TokenServiceImpl.class);

    @Override
    public synchronized IamToken getToken() {
        if (token == null || tokenExp == null || DateUtils.addSeconds(new Date(),
                30).after(tokenExp)) {
            reloadToken();
        }
        return token;
    }

    private void reloadToken() {
        ExchangeTokenRequest payload = ExchangeTokenRequest.builder()
                .grantType(VngCloudClientConstant.GrantType.clientCredentials)
                .build();
        String clientKey = String.format("%s:%s", iamClientConfig.getIamAccessKey(), iamClientConfig.getIamSecretKey());
        IamToken iamToken = null;
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String url = String.format("%s/auth/token", iamClientConfig.getAccountApiUrl());
            RequestBody requestBody = RequestBody.create(gson.toJson(payload), JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader(RestHeaderConstants.AUTHORIZATION, String.format("Basic %s", Base64.getEncoder().encodeToString(clientKey.getBytes())))
                    .build();

            String json = execute(request);
            Type typeToken = new TypeToken<IamToken>() {
            }.getType();
            iamToken = gson.fromJson(json, typeToken);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        DecodedJWT jwt = JWT.decode(iamToken.getAccessToken());
        tokenExp = jwt.getExpiresAt();
        token = iamToken;
    }

    protected String execute(Request request) throws Exception {
        try {
            long timeStamp = System.currentTimeMillis() / 1000;
            request = request.newBuilder()
                    .addHeader("timestamp", Long.toString(timeStamp))
                    .build();

            Gson gson = new Gson();
            logger.info("vmonitor-log-api<--- " + gson.toJson(request));
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String json = Optional.ofNullable(response.body())
                        .orElseThrow(() -> new Exception("Not found body response")).string();
                logger.info("vmonitor-log-api---> " + json);
                return json;
            } else {
                logger.info("vmonitor-log-api---> " + response.message());
                logger.error(response.message());
                String bodyResponse = response.body().string();
                JsonObject obj = gson.fromJson(bodyResponse, JsonObject.class);
                if (obj.has("message")) {
                    throw new Exception(obj.get("message").getAsString());
                } else {
                    throw new Exception("Internal error");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }
}
