package com.transporterLanes.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TransporterAssignmentSolver {

    private BigDecimal bestCost;
    private int bestTransporterCount;
    private Map<Long, QuoteOption> bestAssignments;

    public AssignmentResult solve(List<LaneQuotes> lanes, int maxTransporters) {

        if (lanes == null || lanes.isEmpty()) {
            throw new IllegalArgumentException("No lanes available for assignment.");
        }

        if (maxTransporters <= 0) {
            throw new IllegalArgumentException("maxTransporters must be greater than zero.");
        }

        // Validate and copy lane options.
        List<LaneQuotes> sortedLanes = new ArrayList<>();

        for (LaneQuotes lane : lanes) {
            if (lane.getOptions() == null || lane.getOptions().isEmpty()) {
                throw new IllegalArgumentException(
                    "No transporter quotes available for lane: " + lane.getLaneId()
                );
            }

            List<QuoteOption> options = new ArrayList<>(lane.getOptions());
            options.sort(Comparator.comparing(QuoteOption::getCost));

            sortedLanes.add(new LaneQuotes(lane.getLaneId(), options));
        }

        // Process the most constrained lanes first.
        sortedLanes.sort(
            Comparator.comparingInt((LaneQuotes lane) -> lane.getOptions().size())
                      .thenComparingLong(LaneQuotes::getLaneId)
        );

        int effectiveMax = Math.min(maxTransporters, sortedLanes.size());

        bestCost = null;
        bestTransporterCount = 0;
        bestAssignments = null;

        search(
            sortedLanes,
            0,
            effectiveMax,
            BigDecimal.ZERO,
            new HashSet<>(),
            new HashMap<>()
        );

        if (bestAssignments == null) {
            throw new IllegalStateException(
                "No feasible assignment found within maxTransporters = " + maxTransporters
            );
        }

        List<Assignment> assignments = new ArrayList<>();

        bestAssignments.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> assignments.add(
                new Assignment(
                    entry.getKey(),
                    entry.getValue().getTransporterId()
                )
            ));

        Set<Long> selectedIds = new TreeSet<>();
        for (Assignment assignment : assignments) {
            selectedIds.add(assignment.getTransporterId());
        }

        return new AssignmentResult(
            bestCost,
            assignments,
            new ArrayList<>(selectedIds)
        );
    }

    private void search(
            List<LaneQuotes> lanes,
            int laneIndex,
            int maxTransporters,
            BigDecimal currentCost,
            Set<Long> usedTransporters,
            Map<Long, QuoteOption> currentAssignments) {

        // All lanes have been assigned.
        if (laneIndex == lanes.size()) {
            int usedCount = usedTransporters.size();

            boolean betterCost =
                bestCost == null || currentCost.compareTo(bestCost) < 0;

            boolean sameCostMoreTransporters =
                bestCost != null
                && currentCost.compareTo(bestCost) == 0
                && usedCount > bestTransporterCount;

            if (betterCost || sameCostMoreTransporters) {
                bestCost = currentCost;
                bestTransporterCount = usedCount;
                bestAssignments = new HashMap<>(currentAssignments);
            }

            return;
        }

        // Calculate an optimistic lower bound for the remaining lanes.
        // This deliberately ignores the transporter limit, making it safe
        // as a lower bound.
        BigDecimal lowerBound = currentCost;

        for (int i = laneIndex; i < lanes.size(); i++) {
            BigDecimal cheapest = lanes.get(i).getOptions().get(0).getCost();
            lowerBound = lowerBound.add(cheapest);
        }

        // Use > rather than >= so equal-cost solutions can still
        // be explored for the transporter-count tie-breaker.
        if (bestCost != null && lowerBound.compareTo(bestCost) > 0) {
            return;
        }

        LaneQuotes currentLane = lanes.get(laneIndex);

        for (QuoteOption option : currentLane.getOptions()) {

            long transporterId = option.getTransporterId();

            boolean isNewTransporter = !usedTransporters.contains(transporterId);

            if (isNewTransporter && usedTransporters.size() >= maxTransporters) {
                continue;
            }

            usedTransporters.add(transporterId);
            currentAssignments.put(currentLane.getLaneId(), option);

            search(
                lanes,
                laneIndex + 1,
                maxTransporters,
                currentCost.add(option.getCost()),
                usedTransporters,
                currentAssignments
            );

            // Backtrack.
            currentAssignments.remove(currentLane.getLaneId());

            if (isNewTransporter) {
                usedTransporters.remove(transporterId);
            }
        }
    }

    // Input: all available quotes for one lane.
    public static class LaneQuotes {
        private final long laneId;
        private final List<QuoteOption> options;

        public LaneQuotes(long laneId, List<QuoteOption> options) {
            this.laneId = laneId;
            this.options = options;
        }

        public long getLaneId() {
            return laneId;
        }

        public List<QuoteOption> getOptions() {
            return options;
        }
    }

    // One transporter quotation for one lane.
    public static class QuoteOption {
        private final long transporterId;
        private final BigDecimal cost;

        public QuoteOption(long transporterId, BigDecimal cost) {
            this.transporterId = transporterId;
            this.cost = cost;
        }

        public long getTransporterId() {
            return transporterId;
        }

        public BigDecimal getCost() {
            return cost;
        }
    }

    // One lane-to-transporter assignment.
    public static class Assignment {
        private final long laneId;
        private final long transporterId;

        public Assignment(long laneId, long transporterId) {
            this.laneId = laneId;
            this.transporterId = transporterId;
        }

        public long getLaneId() {
            return laneId;
        }

        public long getTransporterId() {
            return transporterId;
        }
    }

    // Final solver result.
    public static class AssignmentResult {
        private final BigDecimal totalCost;
        private final List<Assignment> assignments;
        private final List<Long> selectedTransporters;

        public AssignmentResult(
                BigDecimal totalCost,
                List<Assignment> assignments,
                List<Long> selectedTransporters) {
            this.totalCost = totalCost;
            this.assignments = assignments;
            this.selectedTransporters = selectedTransporters;
        }

        public BigDecimal getTotalCost() {
            return totalCost;
        }

        public List<Assignment> getAssignments() {
            return assignments;
        }

        public List<Long> getSelectedTransporters() {
            return selectedTransporters;
        }
    }
}