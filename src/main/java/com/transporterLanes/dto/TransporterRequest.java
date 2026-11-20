package com.transporterLanes.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransporterRequest {

    @NotNull(message = "Transporter ID is required")
    @Positive(message = "Transporter ID must be positive")
    private Long id;

    @NotBlank(message = "Transporter name is required")
    private String name;

    @NotEmpty(message = "At least one quote is required")
    @Valid
    private List<LaneQuoteRequest> laneQuotes;

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LaneQuoteRequest> getLaneQuotes() {
        return laneQuotes;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLaneQuotes(List<LaneQuoteRequest> laneQuotes) {
        this.laneQuotes = laneQuotes;
    }
}