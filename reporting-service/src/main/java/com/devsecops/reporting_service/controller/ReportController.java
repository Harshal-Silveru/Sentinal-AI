package com.devsecops.reporting_service.controller;

import com.devsecops.reporting_service.entity.Report;
import com.devsecops.reporting_service.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report createReport(
            @RequestBody Report report) {

        return reportService.createReport(report);
    }

    @GetMapping
    public List<Report> getAllReports() {

        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(
            @PathVariable Long id) {

        return reportService.getReportById(id);
    }

    @GetMapping("/analysis/{analysisId}")
    public List<Report> getReportsByAnalysis(
            @PathVariable Long analysisId) {

        return reportService
                .getReportsByAnalysis(analysisId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(
            @PathVariable Long id) {

        reportService.deleteReport(id);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.devsecops.reporting_service.pdf.PdfReportGenerator pdfGenerator;

    @GetMapping(value = "/{id}/pdf", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public byte[] getReportPdf(@PathVariable Long id) {
        return reportService.generatePdf(id, pdfGenerator);
    }
}
