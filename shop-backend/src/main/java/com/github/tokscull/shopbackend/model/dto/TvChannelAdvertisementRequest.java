package com.github.tokscull.shopbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TvChannelAdvertisementRequest {
    private long duration;
    private long repeatCount;
}
