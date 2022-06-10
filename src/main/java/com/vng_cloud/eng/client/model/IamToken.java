package com.vng_cloud.eng.client.model;

import lombok.Data;

@Data
public class IamToken {
    String accessToken;
    String tokenType;
    Integer expiresIn;
    String refreshToken;
    Integer refreshExpiresIn;
}
