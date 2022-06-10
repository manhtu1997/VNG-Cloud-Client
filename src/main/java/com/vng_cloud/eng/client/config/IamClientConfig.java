package com.vng_cloud.eng.client.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IamClientConfig {
    private String accountApiUrl;
    private String iamAccessKey;
    private String iamSecretKey;
}
