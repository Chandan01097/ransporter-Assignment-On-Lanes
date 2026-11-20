package com.transporterLanes.repository;

import com.transporterLanes.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaneRepository extends JpaRepository<Lane, Long> {
}
