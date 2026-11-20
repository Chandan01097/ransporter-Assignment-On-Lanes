package com.transporterLanes.controller;

import com.transporterLanes.dto.InputRequest;
import com.transporterLanes.dto.AssignmentRequest;
import com.transporterLanes.dto.AssignmentResponse;
import com.transporterLanes.service.TransporterService;
import com.transporterLanes.service.AssignmentOptimizerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/transporters")
@Tag(
    name = "Transporter Assignment",
    description = "APIs for submitting transporter data and optimizing lane assignments"
)
public class TransporterController {

    private final TransporterService transporterService;
    private final AssignmentOptimizerService assignmentOptimizerService;

    // Constructor injection
    public TransporterController(
            TransporterService transporterService,
            AssignmentOptimizerService assignmentOptimizerService) {

        this.transporterService = transporterService;
        this.assignmentOptimizerService = assignmentOptimizerService;
    }

    // API 1: Submit lanes, transporters and quotations
    @Operation(
        summary = "Submit Transporter Input Data",
        description = "Accepts lane details, transporter details and their transportation quotations. Saves the submitted data into the database."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Input data submitted successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        )
    })
    @PostMapping("/input")
    public ResponseEntity<Map<String, String>> submitInput(
            @Valid @RequestBody InputRequest request) {

        String message = transporterService.saveInputData(request);

        return ResponseEntity.ok(
            Map.of(
                "status", "success",
                "message", message
            )
        );
    }

    // API 2: Optimize transporter assignments
    @Operation(
        summary = "Optimize Transporter Assignments",
        description = "Assigns exactly one transporter to every saved lane while minimizing total transportation cost. When multiple assignments have equal minimum cost, the solution using more distinct transporters is preferred, subject to the maximum transporter limit."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transporter assignments optimized successfully",
            content = @Content(
                schema = @Schema(
                    implementation = AssignmentResponse.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request. maxTransporters must be a positive integer."
        ),
        @ApiResponse(
            responseCode = "422",
            description = "No feasible assignment exists within the requested transporter limit or lane coverage requirements."
        )
    })
    @PostMapping("/assignment")
    public ResponseEntity<?> optimizeAssignment(
            @Valid @RequestBody AssignmentRequest request) {

        try {
            AssignmentResponse response =
                assignmentOptimizerService.optimizeAssignment(request);

            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {

            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of(
                        "status", "FAILED",
                        "message", ex.getMessage()
                    ));
        }
    }
}
