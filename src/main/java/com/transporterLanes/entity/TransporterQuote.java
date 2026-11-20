package com.transporterLanes.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "transporter_quotes",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"transporter_id", "lane_id"}
        )
    }
)
public class TransporterQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", nullable = false)
    private Transporter transporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lane_id", nullable = false)
    private Lane lane;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quote;

    // No-argument constructor
    public TransporterQuote() {
    }

    // All-argument constructor
    public TransporterQuote(Long id, Transporter transporter, Lane lane, BigDecimal quote) {
        this.id = id;
        this.transporter = transporter;
        this.lane = lane;
        this.quote = quote;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public Lane getLane() {
        return lane;
    }

    public BigDecimal getQuote() {
        return quote;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }
}