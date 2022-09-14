package com.github.tokscull.channel;

import com.github.tokscull.channel.spi.TvChannelAdvertisementProvider;

import java.math.BigDecimal;

public class SimpleTvChannelAdvertisementProvider implements TvChannelAdvertisementProvider {

    @Override
    public String getName() {
        return "SimpleTvChannel";
    }

    @Override
    public BigDecimal getAdvertisementPrice(long duration, long repeatCount) {
        return BigDecimal.valueOf(duration * 0.335 + repeatCount);
    }
}
