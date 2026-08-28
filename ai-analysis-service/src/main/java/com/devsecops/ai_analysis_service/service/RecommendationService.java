package com.devsecops.ai_analysis_service.service;


import com.devsecops.ai_analysis_service.entity.Analysis;
import com.devsecops.ai_analysis_service.entity.Recommendation;
import com.devsecops.ai_analysis_service.repository.AnalysisRepository;
import com.devsecops.ai_analysis_service.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final AnalysisRepository analysisRepository;

    public RecommendationService(
            RecommendationRepository recommendationRepository,
            AnalysisRepository analysisRepository) {

        this.recommendationRepository = recommendationRepository;
        this.analysisRepository = analysisRepository;
    }

    public Recommendation createRecommendation(
            Recommendation recommendation) {

        Analysis analysis =
                analysisRepository.findById(
                        recommendation.getAnalysisId()
                ).orElseThrow(() ->
                        new RuntimeException("Analysis not found"));

        if (recommendation.getPriority() == null ||
                recommendation.getPriority().isBlank()) {

            recommendation.setPriority(
                    determinePriority(analysis.getRiskScore())
            );
        }

        return recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    public Recommendation getRecommendationById(Long id) {

        return recommendationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Recommendation not found"
                        ));
    }

    public List<Recommendation> getRecommendationsByAnalysis(
            Long analysisId) {

        return recommendationRepository
                .findByAnalysisId(analysisId);
    }

    public void deleteRecommendation(Long id) {

        if (!recommendationRepository.existsById(id)) {
            throw new RuntimeException(
                    "Recommendation not found"
            );
        }

        recommendationRepository.deleteById(id);
    }

    private String determinePriority(Double riskScore) {

        if (riskScore >= 75) {
            return "HIGH";
        }

        if (riskScore >= 50) {
            return "MEDIUM";
        }

        return "LOW";
    }
}
