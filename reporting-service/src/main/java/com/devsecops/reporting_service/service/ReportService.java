package com.devsecops.reporting_service.service;


import com.devsecops.reporting_service.client.AIAnalysisServiceClient;
import com.devsecops.reporting_service.dto.AnalysisResponse;
import com.devsecops.reporting_service.entity.Report;
import com.devsecops.reporting_service.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final AIAnalysisServiceClient analysisClient;

    public ReportService(
            ReportRepository reportRepository,
            AIAnalysisServiceClient analysisClient) {

        this.reportRepository = reportRepository;
        this.analysisClient = analysisClient;
    }

    public Report createReport(Report report) {

        AnalysisResponse analysis =
                analysisClient.getAnalysisById(
                        report.getAnalysisId()
                );

        if (analysis == null) {
            throw new RuntimeException(
                    "Analysis does not exist"
            );
        }

        report.setGeneratedDate(
                LocalDateTime.now()
        );

        if (report.getStatus() == null ||
                report.getStatus().isBlank()) {

            report.setStatus("GENERATED");
        }

        if (report.getSummary() == null ||
                report.getSummary().isBlank()) {

            report.setSummary(
                    generateSummary(analysis)
            );
        }

        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {

        return reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Report not found"
                        ));
    }

    public List<Report> getReportsByAnalysis(
            Long analysisId) {

        return reportRepository
                .findByAnalysisId(analysisId);
    }

    public void deleteReport(Long id) {

        if (!reportRepository.existsById(id)) {

            throw new RuntimeException(
                    "Report not found"
            );
        }

        reportRepository.deleteById(id);
    }

    private String generateSummary(
            AnalysisResponse analysis) {

        return "Security analysis report generated for "
                + "analysis ID "
                + analysis.getId()
                + ". Risk score: "
                + analysis.getRiskScore()
                + ". Explanation: "
                + analysis.getExplanation();
    }
}
