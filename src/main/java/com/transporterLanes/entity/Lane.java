package com.transporterLanes.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lanes")
public class Lane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @OneToMany(mappedBy = "lane")
    private List<TransporterQuote> quotes = new ArrayList<>();

    // No-argument constructor
    public Lane() {
    }

    // All-argument constructor
    public Lane(Long id, String origin, String destination, List<TransporterQuote> quotes) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.quotes = quotes;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public List<TransporterQuote> getQuotes() {
        return quotes;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setQuotes(List<TransporterQuote> quotes) {
        this.quotes = quotes;
    }
}