package io.github.manhtu1997.vng_cloud_client.response;

import lombok.Data;

@Data
public class ExportLogResponse {
    private String id;
    private Integer total;
    private Integer processedAmount;
    private String status;
}
