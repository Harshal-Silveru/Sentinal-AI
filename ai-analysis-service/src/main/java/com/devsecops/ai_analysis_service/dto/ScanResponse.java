package com.devsecops.ai_analysis_service.dto;

import java.time.LocalDateTime;

public class ScanResponse {

    private Long id;
    private Long projectId;
    private LocalDateTime scanDate;
    private String status;
    private Double securityScore;

    public ScanResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(LocalDateTime scanDate) {
        this.scanDate = scanDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSecurityScore() {
        return securityScore;
    }

    public void setSecurityScore(Double securityScore) {
        this.securityScore = securityScore;
    }
}
