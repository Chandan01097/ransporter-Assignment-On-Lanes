
package com.transporterLanes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporterLanes.algorithm.TransporterAssignmentSolver;
import com.transporterLanes.algorithm.TransporterAssignmentSolver.Assignment;
import com.transporterLanes.algorithm.TransporterAssignmentSolver.AssignmentResult;
import com.transporterLanes.algorithm.TransporterAssignmentSolver.LaneQuotes;
import com.transporterLanes.algorithm.TransporterAssignmentSolver.QuoteOption;

import com.transporterLanes.dto.AssignmentRequest;
import com.transporterLanes.dto.AssignmentResponse;
import com.transporterLanes.entity.TransporterQuote;
import com.transporterLanes.repository.TransporterQuoteRepository;

@Service
public class AssignmentOptimizerService {

    private final TransporterQuoteRepository quoteRepository;
    private final TransporterAssignmentSolver solver;

    public AssignmentOptimizerService(
            TransporterQuoteRepository quoteRepository) {

        this.quoteRepository = quoteRepository;
        this.solver = new TransporterAssignmentSolver();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse optimizeAssignment(AssignmentRequest request) {

        // 1. Fetch all available quotations.
        List<TransporterQuote> quotes = quoteRepository.findAll();

        if (quotes.isEmpty()) {
            throw new IllegalStateException(
                "No transporter quotations found. Please submit input data first."
            );
        }

        // 2. Group quotations by lane ID.
        Map<Long, List<QuoteOption>> quotesByLane = new HashMap<>();

        for (TransporterQuote quote : quotes) {

            Long laneId = quote.getLane().getId();
            Long transporterId = quote.getTransporter().getId();

            QuoteOption option = new QuoteOption(
                transporterId,
                quote.getQuote()
            );

            quotesByLane
                .computeIfAbsent(laneId, key -> new ArrayList<>())
                .add(option);
        }

        // 3. Convert grouped quotations into solver input.
        List<LaneQuotes> solverInput = new ArrayList<>();

        quotesByLane.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> solverInput.add(
                new LaneQuotes(
                    entry.getKey(),
                    entry.getValue()
                )
            ));

        // 4. Execute optimization.
        AssignmentResult result = solver.solve(
            solverInput,
            request.getMaxTransporters()
        );

        // 5. Prepare API response.
        AssignmentResponse response = new AssignmentResponse();

        response.setStatus("SUCCESS");
        response.setTotalCost(result.getTotalCost());

        List<AssignmentResponse.AssignmentItem> assignmentItems =
            new ArrayList<>();

        for (Assignment assignment : result.getAssignments()) {

            AssignmentResponse.AssignmentItem item =
                new AssignmentResponse.AssignmentItem();

            item.setLaneId(assignment.getLaneId());
            item.setTransporterId(assignment.getTransporterId());

            assignmentItems.add(item);
        }

        response.setAssignments(assignmentItems);
        response.setSelectedTransporters(
            result.getSelectedTransporters()
        );

        return response;
    }
}
