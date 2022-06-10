package com.vng_cloud.eng.client.response;

import lombok.Data;

@Data
public class ExportLogResponse {
    private String id;
    private Integer total;
    private Integer processedAmount;
    private String status;
}
