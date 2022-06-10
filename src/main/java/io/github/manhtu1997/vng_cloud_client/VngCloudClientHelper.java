package io.github.manhtu1997.vng_cloud_client;

import io.github.manhtu1997.vng_cloud_client.util.HttpClientUtil;
import io.github.manhtu1997.vng_cloud_client.config.IamClientConfig;
import io.github.manhtu1997.vng_cloud_client.service.TokenService;
import io.github.manhtu1997.vng_cloud_client.service.impl.LogServiceImpl;
import io.github.manhtu1997.vng_cloud_client.config.ClientConfig;
import io.github.manhtu1997.vng_cloud_client.constant.VngCloudClientConstant;
import io.github.manhtu1997.vng_cloud_client.service.LogService;
import io.github.manhtu1997.vng_cloud_client.service.impl.TokenServiceImpl;
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
