package com.transporterLanes.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class InputRequest {

    @NotEmpty(message = "At least one lane is required")
    @Valid
    private List<LaneRequest> lanes;

    @NotEmpty(message = "At least one transporter is required")
    @Valid
    private List<TransporterRequest> transporters;

    // Getters

    public List<LaneRequest> getLanes() {
        return lanes;
    }

    public List<TransporterRequest> getTransporters() {
        return transporters;
    }

    // Setters

    public void setLanes(List<LaneRequest> lanes) {
        this.lanes = lanes;
    }

    public void setTransporters(List<TransporterRequest> transporters) {
        this.transporters = transporters;
    }
}