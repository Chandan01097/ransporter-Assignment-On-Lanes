package com.transporterLanes.dto;

import java.math.BigDecimal;
import java.util.List;

public class AssignmentResponse {

    private String status;
    private BigDecimal totalCost;
    private List<AssignmentItem> assignments;
    private List<Long> selectedTransporters;

    // No-argument constructor
    public AssignmentResponse() {
    }

    // All-argument constructor
    public AssignmentResponse(
            String status,
            BigDecimal totalCost,
            List<AssignmentItem> assignments,
            List<Long> selectedTransporters) {

        this.status = status;
        this.totalCost = totalCost;
        this.assignments = assignments;
        this.selectedTransporters = selectedTransporters;
    }

    // Getters

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public List<AssignmentItem> getAssignments() {
        return assignments;
    }

    public List<Long> getSelectedTransporters() {
        return selectedTransporters;
    }

    // Setters

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setAssignments(List<AssignmentItem> assignments) {
        this.assignments = assignments;
    }

    public void setSelectedTransporters(List<Long> selectedTransporters) {
        this.selectedTransporters = selectedTransporters;
    }

    // Inner class
    public static class AssignmentItem {

        private Long laneId;
        private Long transporterId;

        // No-argument constructor
        public AssignmentItem() {
        }

        // All-argument constructor
        public AssignmentItem(Long laneId, Long transporterId) {
            this.laneId = laneId;
            this.transporterId = transporterId;
        }

        // Getters

        public Long getLaneId() {
            return laneId;
        }

        public Long getTransporterId() {
            return transporterId;
        }

        // Setters

        public void setLaneId(Long laneId) {
            this.laneId = laneId;
        }

        public void setTransporterId(Long transporterId) {
            this.transporterId = transporterId;
        }
    }
}