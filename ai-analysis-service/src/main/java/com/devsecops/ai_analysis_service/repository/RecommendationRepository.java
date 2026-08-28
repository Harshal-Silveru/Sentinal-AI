package com.devsecops.ai_analysis_service.repository;


import com.devsecops.ai_analysis_service.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByAnalysisId(Long analysisId);
}