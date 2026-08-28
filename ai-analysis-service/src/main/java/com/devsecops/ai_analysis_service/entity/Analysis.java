package com.devsecops.ai_analysis_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scanId;

    @Column(nullable = false)
    private Double riskScore;

    @Column(nullable = false, length = 1000)
    private String explanation;

    public Analysis() {
    }

    public Analysis(Long scanId, Double riskScore, String explanation) {
        this.scanId = scanId;
        this.riskScore = riskScore;
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }

    public Double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
