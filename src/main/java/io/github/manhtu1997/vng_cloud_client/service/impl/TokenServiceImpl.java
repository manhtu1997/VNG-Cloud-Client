package io.github.manhtu1997.vng_cloud_client.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.manhtu1997.vng_cloud_client.config.IamClientConfig;
import io.github.manhtu1997.vng_cloud_client.exception.HttpHandledException;
import io.github.manhtu1997.vng_cloud_client.model.IamToken;
import io.github.manhtu1997.vng_cloud_client.request.ExchangeTokenRequest;
import io.github.manhtu1997.vng_cloud_client.constant.VngCloudClientConstant;
import io.github.manhtu1997.vng_cloud_client.service.TokenService;
import io.github.manhtu1997.vng_cloud_client.constant.RestHeaderConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
    public static final MediaType FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

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
                .scope(VngCloudClientConstant.Scope.email)
                .build();
        String clientKey = String.format("%s:%s", iamClientConfig.getIamAccessKey(), iamClientConfig.getIamSecretKey());
        IamToken iamToken = null;
        try {
            Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String url = String.format("%s/v1/auth/token", iamClientConfig.getAccountApiUrl());
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
        } catch (HttpHandledException hhe){
            logger.error(hhe.getMessage(), hhe);
            throw hhe;
        } catch (IOException ioe){
            logger.error(ioe.getMessage(), ioe);
            throw new HttpHandledException(ioe.getMessage(),500);
        }
        DecodedJWT jwt = JWT.decode(iamToken.getAccessToken());
        tokenExp = jwt.getExpiresAt();
        token = iamToken;
    }

    protected String execute(Request request) throws HttpHandledException, IOException {
        long timeStamp = System.currentTimeMillis() / 1000;
        request = request.newBuilder()
                .addHeader("timestamp", Long.toString(timeStamp))
                .build();

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        logger.info("vmonitor-log-api<--- " + gson.toJson(request));
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String json = Optional.ofNullable(response.body())
                    .orElseThrow(() -> new HttpHandledException("Not found body response",500)).string();
            logger.info("vmonitor-log-api---> " + json);
            return json;
        } else {
            logger.info("vmonitor-log-api---> " + response.message());
            logger.error(response.message());
            String bodyResponse = response.body().string();
            JsonObject obj = gson.fromJson(bodyResponse, JsonObject.class);
            if (obj.has("message")) {
                throw new HttpHandledException(obj.get("message").getAsString(),response.code());
            } else {
                throw new HttpHandledException("Internal error",500);
            }
        }
    }
}
