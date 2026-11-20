package com.transporterLanes.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AssignmentRequest {

    @NotNull(message = "Maximum transporters is required")
    @Positive(message = "Maximum transporters must be positive")
    private Integer maxTransporters;

    // Getter
    public Integer getMaxTransporters() {
        return maxTransporters;
    }

    // Setter
    public void setMaxTransporters(Integer maxTransporters) {
        this.maxTransporters = maxTransporters;
    }
}