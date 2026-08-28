package com.devsecops.reporting_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long analysisId;

    @Column(nullable = false, length = 150)
    private String reportName;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @Column(nullable = false, length = 2000)
    private String summary;

    @Column(nullable = false, length = 30)
    private String status;

    public Report() {
    }

    public Report(Long analysisId,
                  String reportName,
                  LocalDateTime generatedDate,
                  String summary,
                  String status) {

        this.analysisId = analysisId;
        this.reportName = reportName;
        this.generatedDate = generatedDate;
        this.summary = summary;
        this.status = status;
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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
