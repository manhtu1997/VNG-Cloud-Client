package io.github.manhtu1997.vng_cloud_client.model;

import lombok.Data;

@Data
public class IamToken {
    String accessToken;
    String tokenType;
    Integer expiresIn;
    String refreshToken;
    Integer refreshExpiresIn;
}
