package com.transporterLanes.repository;

import com.transporterLanes.entity.TransporterQuote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransporterQuoteRepository extends JpaRepository<TransporterQuote, Long> {

    List<TransporterQuote> findAllByLaneId(Long laneId);

    List<TransporterQuote> findAllByTransporterId(Long transporterId);
}
