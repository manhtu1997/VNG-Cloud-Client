package com.vng_cloud.eng.client;

import com.vng_cloud.eng.client.config.IamClientConfig;
import com.vng_cloud.eng.client.service.TokenService;
import com.vng_cloud.eng.client.service.impl.LogServiceImpl;
import com.vng_cloud.eng.client.config.ClientConfig;
import com.vng_cloud.eng.client.constant.VngCloudClientConstant;
import com.vng_cloud.eng.client.service.LogService;
import com.vng_cloud.eng.client.service.impl.TokenServiceImpl;
import com.vng_cloud.eng.client.util.HttpClientUtil;
import org.jetbrains.annotations.NotNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

public class VngCloudClientHelper {
    private static Map<String,String> mapServiceUrl;
    private static IamClientConfig iamClientConfig;

    public static void initConfig(@NotNull String accountApiUrl,@NotNull String iamAccessKey
            ,@NotNull String iamSecretKey,@NotNull Map<String,String> mapServiceUrlArg) {
        iamClientConfig = IamClientConfig.builder()
                .accountApiUrl(accountApiUrl)
                .iamAccessKey(iamAccessKey)
                .iamSecretKey(iamSecretKey)
                .build();
        mapServiceUrl=mapServiceUrlArg;
    }

    public static TokenService getTokenService()
            throws NoSuchAlgorithmException, KeyManagementException {
        return new TokenServiceImpl(HttpClientUtil.getUnsafeHttpClient(), iamClientConfig);
    }

    public static LogService getLogService()
            throws NoSuchAlgorithmException, KeyManagementException {
        String logApiUrl= Optional.ofNullable(mapServiceUrl.get(VngCloudClientConstant.Service.logApiUrl))
                .orElseThrow(()->new NullPointerException("logApiUrl must be not null"));
        ClientConfig clientConfig= ClientConfig.builder()
                .baseUrl(logApiUrl)
                .build();
        return new LogServiceImpl(HttpClientUtil.getUnsafeHttpClient(), clientConfig);
    }
}
