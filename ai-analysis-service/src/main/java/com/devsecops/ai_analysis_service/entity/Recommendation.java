package com.devsecops.ai_analysis_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long analysisId;

    @Column(nullable = false, length = 1000)
    private String suggestedFix;

    @Column(nullable = false, length = 30)
    private String priority;

    public Recommendation() {
    }

    public Recommendation(Long analysisId, String suggestedFix, String priority) {
        this.analysisId = analysisId;
        this.suggestedFix = suggestedFix;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public void setSuggestedFix(String suggestedFix) {
        this.suggestedFix = suggestedFix;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
