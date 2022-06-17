package io.github.manhtu1997.vng_cloud_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogQueryValue {
    List<TypeValue> filter;
    List<TypeValue> should;
    List<TypeValue> must;
    List<TypeValue> mustNot;
}
