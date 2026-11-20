package com.transporterLanes.repository;

import com.transporterLanes.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransporterRepository
        extends JpaRepository<Transporter, Long> {

}