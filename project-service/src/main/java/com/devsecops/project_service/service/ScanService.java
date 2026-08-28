package com.devsecops.project_service.service;

import com.devsecops.project_service.entity.Scan;
import com.devsecops.project_service.repository.ScanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanService {

    private final ScanRepository scanRepository;

    public ScanService(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    public Scan createScan(Scan scan) {

        scan.setScanDate(LocalDateTime.now());

        if (scan.getStatus() == null ||
                scan.getStatus().isBlank()) {

            scan.setStatus("STARTED");
        }

        if (scan.getSecurityScore() == null) {
            scan.setSecurityScore(0.0);
        }

        return scanRepository.save(scan);
    }

    public List<Scan> getAllScans() {
        return scanRepository.findAll();
    }

    public Scan getScanById(Long id) {

        return scanRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Scan not found"));
    }

    public List<Scan> getScansByProject(Long projectId) {
        return scanRepository.findByProjectId(projectId);
    }
}
