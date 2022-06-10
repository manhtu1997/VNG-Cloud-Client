package com.vng_cloud.eng.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeTokenRequest {
    String grantType;
    String code;
    String refreshToken;
    String redirectUri;
}
