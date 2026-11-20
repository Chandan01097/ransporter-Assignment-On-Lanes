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
@Table(name = "transporters")
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "transporter")
    private List<TransporterQuote> quotes = new ArrayList<>();

    // No-argument constructor
    public Transporter() {
    }

    // All-argument constructor
    public Transporter(Long id, String name, List<TransporterQuote> quotes) {
        this.id = id;
        this.name = name;
        this.quotes = quotes;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TransporterQuote> getQuotes() {
        return quotes;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuotes(List<TransporterQuote> quotes) {
        this.quotes = quotes;
    }
}