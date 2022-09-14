package com.github.tokscull.channel.spi;

import java.math.BigDecimal;

/**
 * TvChannelAdvertisementProvider interface
 */
public interface TvChannelAdvertisementProvider {

    /**
     * TvChannelAdvertisementProvider getName method
     * @return string value
     */
    String getName();


    /**
     * TvChannelAdvertisementProvider getAdvertisementPrice method
     * @param duration - duration of advertisement in ms
     * @param repeatCount - repeatCount
     * @return int value
     */
    BigDecimal getAdvertisementPrice(long duration, long repeatCount);
}
