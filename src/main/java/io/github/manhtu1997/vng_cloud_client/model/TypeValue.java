package io.github.manhtu1997.vng_cloud_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeValue {
    private String type;
    private Object value;
}
