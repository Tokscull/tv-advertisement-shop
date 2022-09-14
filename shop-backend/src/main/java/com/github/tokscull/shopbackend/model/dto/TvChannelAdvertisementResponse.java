package com.github.tokscull.shopbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TvChannelAdvertisementResponse {
    private String channelName;
    private BigDecimal price;
}
