package com.devsecops.ai_analysis_service.controller;

import com.devsecops.ai_analysis_service.entity.Analysis;
import com.devsecops.ai_analysis_service.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyses")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Analysis createAnalysis(
            @RequestBody Analysis analysis) {

        return analysisService.createAnalysis(analysis);
    }

    @GetMapping
    public List<Analysis> getAllAnalyses() {
        return analysisService.getAllAnalyses();
    }

    @GetMapping("/{id}")
    public Analysis getAnalysisById(
            @PathVariable Long id) {

        return analysisService.getAnalysisById(id);
    }

    @GetMapping("/scan/{scanId}")
    public List<Analysis> getAnalysesByScan(
            @PathVariable Long scanId) {

        return analysisService.getAnalysesByScan(scanId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnalysis(
            @PathVariable Long id) {

        analysisService.deleteAnalysis(id);
    }
}
