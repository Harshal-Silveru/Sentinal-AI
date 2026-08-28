package com.devsecops.ai_analysis_service.service;


import com.devsecops.ai_analysis_service.client.ProjectScanningServiceClient;
import com.devsecops.ai_analysis_service.dto.ScanResponse;
import com.devsecops.ai_analysis_service.entity.Analysis;
import com.devsecops.ai_analysis_service.repository.AnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ProjectScanningServiceClient scanClient;

    public AnalysisService(
            AnalysisRepository analysisRepository,
            ProjectScanningServiceClient scanClient) {

        this.analysisRepository = analysisRepository;
        this.scanClient = scanClient;
    }

    public Analysis createAnalysis(Analysis analysis) {

        ScanResponse scan =
                scanClient.getScanById(analysis.getScanId());

        if (scan == null) {
            throw new RuntimeException("Scan does not exist");
        }

        if (analysis.getRiskScore() == null) {
            analysis.setRiskScore(
                    calculateRiskScore(scan.getSecurityScore())
            );
        }

        if (analysis.getExplanation() == null ||
                analysis.getExplanation().isBlank()) {

            analysis.setExplanation(
                    generateExplanation(
                            analysis.getRiskScore()
                    )
            );
        }

        return analysisRepository.save(analysis);
    }

    public List<Analysis> getAllAnalyses() {
        return analysisRepository.findAll();
    }

    public Analysis getAnalysisById(Long id) {

        return analysisRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Analysis not found"));
    }

    public List<Analysis> getAnalysesByScan(Long scanId) {
        return analysisRepository.findByScanId(scanId);
    }

    public void deleteAnalysis(Long id) {

        if (!analysisRepository.existsById(id)) {
            throw new RuntimeException("Analysis not found");
        }

        analysisRepository.deleteById(id);
    }

    private Double calculateRiskScore(Double securityScore) {

        if (securityScore == null) {
            return 50.0;
        }

        double risk = 100.0 - securityScore;

        return Math.max(0.0, Math.min(100.0, risk));
    }

    private String generateExplanation(Double riskScore) {

        if (riskScore >= 75) {
            return "High security risk detected. Immediate remediation is recommended.";
        }

        if (riskScore >= 50) {
            return "Medium security risk detected. Security issues should be reviewed and fixed.";
        }

        if (riskScore >= 25) {
            return "Low to moderate security risk detected. Consider applying the recommended security improvements.";
        }

        return "Minimal security risk detected. The scan indicates a relatively secure project.";
    }
}