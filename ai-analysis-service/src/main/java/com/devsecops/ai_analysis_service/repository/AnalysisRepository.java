package com.devsecops.ai_analysis_service.repository;


import com.devsecops.ai_analysis_service.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository
        extends JpaRepository<Analysis, Long> {

    List<Analysis> findByScanId(Long scanId);
}
