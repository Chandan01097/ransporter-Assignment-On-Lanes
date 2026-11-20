package com.transporterLanes.service;

import com.transporterLanes.dto.*;
import com.transporterLanes.entity.*;
import com.transporterLanes.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransporterService {

    private final LaneRepository laneRepository;
    private final TransporterRepository transporterRepository;
    private final TransporterQuoteRepository quoteRepository;

    // Manual constructor for dependency injection
    public TransporterService(
            LaneRepository laneRepository,
            TransporterRepository transporterRepository,
            TransporterQuoteRepository quoteRepository) {

        this.laneRepository = laneRepository;
        this.transporterRepository = transporterRepository;
        this.quoteRepository = quoteRepository;
    }

    @Transactional
    public String saveInputData(InputRequest request) {

        // Validate duplicate lane IDs
        Set<Long> laneIds = new HashSet<>();

        for (LaneRequest lane : request.getLanes()) {

            if (!laneIds.add(lane.getId())) {
                throw new IllegalArgumentException(
                    "Duplicate lane ID: " + lane.getId()
                );
            }
        }

        // Validate duplicate transporter IDs
        Set<Long> transporterIds = new HashSet<>();

        for (TransporterRequest transporter : request.getTransporters()) {

            if (!transporterIds.add(transporter.getId())) {
                throw new IllegalArgumentException(
                    "Duplicate transporter ID: " + transporter.getId()
                );
            }
        }

        // Validate quote references and duplicate lane quotes
        for (TransporterRequest transporter : request.getTransporters()) {

            Set<Long> quotedLaneIds = new HashSet<>();

            for (LaneQuoteRequest quote : transporter.getLaneQuotes()) {

                if (!laneIds.contains(quote.getLaneId())) {
                    throw new IllegalArgumentException(
                        "Invalid lane ID: " + quote.getLaneId()
                    );
                }

                if (!quotedLaneIds.add(quote.getLaneId())) {
                    throw new IllegalArgumentException(
                        "Duplicate quote for lane ID: " + quote.getLaneId()
                    );
                }
            }
        }

        // Save lanes
        List<Lane> lanes = request.getLanes()
            .stream()
            .map(dto -> {
                Lane lane = new Lane();
                lane.setId(dto.getId());
                lane.setOrigin(dto.getOrigin());
                lane.setDestination(dto.getDestination());
                return lane;
            })
            .collect(Collectors.toList());

        List<Lane> savedLanes = laneRepository.saveAll(lanes);

        Map<Long, Lane> laneMap = savedLanes.stream()
            .collect(Collectors.toMap(
                Lane::getId,
                lane -> lane
            ));

        // Save transporters
        List<Transporter> transporters = request.getTransporters()
            .stream()
            .map(dto -> {
                Transporter transporter = new Transporter();
                transporter.setId(dto.getId());
                transporter.setName(dto.getName());
                return transporter;
            })
            .collect(Collectors.toList());

        List<Transporter> savedTransporters =
            transporterRepository.saveAll(transporters);

        Map<Long, Transporter> transporterMap =
            savedTransporters.stream()
                .collect(Collectors.toMap(
                    Transporter::getId,
                    transporter -> transporter
                ));

        // Save transporter quotes
        List<TransporterQuote> quotes = new ArrayList<>();

        for (TransporterRequest transporterDTO : request.getTransporters()) {

            Transporter transporter =
                transporterMap.get(transporterDTO.getId());

            for (LaneQuoteRequest quoteDTO : transporterDTO.getLaneQuotes()) {

                TransporterQuote quote = new TransporterQuote();

                quote.setTransporter(transporter);
                quote.setLane(laneMap.get(quoteDTO.getLaneId()));
                quote.setQuote(quoteDTO.getQuote());

                quotes.add(quote);
            }
        }

        quoteRepository.saveAll(quotes);

        return "Input data saved successfully.";
    }
}