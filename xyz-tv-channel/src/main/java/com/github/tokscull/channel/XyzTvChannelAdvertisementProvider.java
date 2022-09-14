package com.github.tokscull.channel;

import com.github.tokscull.channel.spi.TvChannelAdvertisementProvider;

import java.math.BigDecimal;

public class XyzTvChannelAdvertisementProvider implements TvChannelAdvertisementProvider {

    @Override
    public String getName() {
        return "XyzTvChannel";
    }

    @Override
    public BigDecimal getAdvertisementPrice(long duration, long repeatCount) {
        return BigDecimal.valueOf(duration * 0.777 + repeatCount * 1.2);
    }
}
