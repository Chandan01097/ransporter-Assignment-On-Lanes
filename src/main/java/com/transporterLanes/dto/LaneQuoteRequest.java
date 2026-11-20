package com.transporterLanes.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LaneQuoteRequest {

    @NotNull(message = "Lane ID is required")
    @Positive(message = "Lane ID must be positive")
    private Long laneId;

    @NotNull(message = "Quote is required")
    @Positive(message = "Quote must be positive")
    private BigDecimal quote;

    // Getters

    public Long getLaneId() {
        return laneId;
    }

    public BigDecimal getQuote() {
        return quote;
    }

    // Setters

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }
}