package io.github.manhtu1997.vng_cloud_client.config;

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
