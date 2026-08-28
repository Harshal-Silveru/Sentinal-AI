package com.devsecops.ai_analysis_service.controller;

import com.devsecops.ai_analysis_service.entity.Recommendation;
import com.devsecops.ai_analysis_service.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {

        this.recommendationService = recommendationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recommendation createRecommendation(
            @RequestBody Recommendation recommendation) {

        return recommendationService
                .createRecommendation(recommendation);
    }

    @GetMapping
    public List<Recommendation> getAllRecommendations() {
        return recommendationService
                .getAllRecommendations();
    }

    @GetMapping("/{id}")
    public Recommendation getRecommendationById(
            @PathVariable Long id) {

        return recommendationService
                .getRecommendationById(id);
    }

    @GetMapping("/analysis/{analysisId}")
    public List<Recommendation> getRecommendationsByAnalysis(
            @PathVariable Long analysisId) {

        return recommendationService
                .getRecommendationsByAnalysis(analysisId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecommendation(
            @PathVariable Long id) {

        recommendationService
                .deleteRecommendation(id);
    }
}
