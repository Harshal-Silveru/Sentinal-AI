package com.devsecops.reporting_service.repository;


import com.devsecops.reporting_service.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository
        extends JpaRepository<Report, Long> {

    List<Report> findByAnalysisId(Long analysisId);
}
