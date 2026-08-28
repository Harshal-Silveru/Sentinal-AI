package com.devsecops.project_service.repository;

import com.devsecops.project_service.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanRepository extends JpaRepository<Scan, Long> {

    List<Scan> findByProjectId(Long projectId);
}
