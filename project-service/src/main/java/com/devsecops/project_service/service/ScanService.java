package com.devsecops.project_service.service;

import com.devsecops.project_service.entity.Scan;
import com.devsecops.project_service.repository.ScanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanService {

    private final ScanRepository scanRepository;
    private final com.devsecops.project_service.repository.FindingRepository findingRepository;
    private final ProjectService projectService;
    private final com.devsecops.project_service.parsing.JavaParserService javaParserService;
    private final com.devsecops.project_service.rules.RuleEngine ruleEngine;

    public ScanService(ScanRepository scanRepository,
                       com.devsecops.project_service.repository.FindingRepository findingRepository,
                       ProjectService projectService,
                       com.devsecops.project_service.parsing.JavaParserService javaParserService,
                       com.devsecops.project_service.rules.RuleEngine ruleEngine) {
        this.scanRepository = scanRepository;
        this.findingRepository = findingRepository;
        this.projectService = projectService;
        this.javaParserService = javaParserService;
        this.ruleEngine = ruleEngine;
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

        Scan savedScan = scanRepository.save(scan);
        try {
            runScanPipeline(savedScan);
        } catch (Exception e) {
            savedScan.setStatus("FAILED");
            scanRepository.save(savedScan);
            throw new RuntimeException("Scan failed: " + e.getMessage(), e);
        }
        return savedScan;
    }

    public void runScanPipeline(Scan scan) throws Exception {
        com.devsecops.project_service.entity.Project project = projectService.getProjectById(scan.getProjectId());
        if (project.getExtractionPath() == null) {
            throw new RuntimeException("Project not extracted yet");
        }

        List<com.devsecops.project_service.parsing.MethodUnit> units = javaParserService.parseProject(project.getExtractionPath());

        List<com.devsecops.project_service.entity.Finding> allFindings = new java.util.ArrayList<>();
        for (com.devsecops.project_service.parsing.MethodUnit unit : units) {
            List<com.devsecops.project_service.rules.RuleResult> results = ruleEngine.evaluateAll(unit);
            for (com.devsecops.project_service.rules.RuleResult res : results) {
                if (res.isHit()) {
                    com.devsecops.project_service.entity.Finding finding = new com.devsecops.project_service.entity.Finding(
                            scan.getId(), res.getRuleName(), res.getMessage(),
                            res.getSeverity().name(), unit.getFilePath(), unit.getClassName(), unit.getMethodName());
                    allFindings.add(finding);
                }
            }
        }
        findingRepository.saveAll(allFindings);

        scan.setStatus("COMPLETED");
        scanRepository.save(scan);
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

    public List<com.devsecops.project_service.entity.Finding> getFindingsByScanId(Long scanId) {
        return findingRepository.findByScanId(scanId);
    }

    public com.devsecops.project_service.dto.SelectedCodeResponse getSelectedCodeForScan(Long scanId, com.devsecops.project_service.srcs.SrcsScorer srcsScorer, com.devsecops.project_service.srcs.ContextExpander contextExpander) throws Exception {
        Scan scan = getScanById(scanId);
        com.devsecops.project_service.entity.Project project = projectService.getProjectById(scan.getProjectId());
        
        List<com.devsecops.project_service.parsing.MethodUnit> allUnits = javaParserService.parseProject(project.getExtractionPath());
        List<com.devsecops.project_service.entity.Finding> allFindings = findingRepository.findByScanId(scanId);
        
        List<com.devsecops.project_service.srcs.SrcsScorer.ScoredUnit> allScored = srcsScorer.scoreAndSelect(allUnits, allFindings);
        
        int top20Pct = (int) Math.ceil(allUnits.size() * 0.20);
        int selectCount = Math.min(top20Pct, 40);
        if (selectCount == 0 && !allScored.isEmpty()) selectCount = 1;
        
        List<com.devsecops.project_service.srcs.SrcsScorer.ScoredUnit> selectedScored = allScored.stream().limit(selectCount).collect(java.util.stream.Collectors.toList());
        
        List<com.devsecops.project_service.parsing.MethodUnit> selectedUnits = new java.util.ArrayList<>();
        for (com.devsecops.project_service.srcs.SrcsScorer.ScoredUnit su : selectedScored) {
            selectedUnits.add(su.unit);
        }
        
        List<com.devsecops.project_service.parsing.MethodUnit> expandedUnits = contextExpander.expandContext(selectedScored, allUnits);
        
        com.devsecops.project_service.dto.SelectedCodeResponse.Stats stats = new com.devsecops.project_service.dto.SelectedCodeResponse.Stats();
        stats.totalFiles = (int) allUnits.stream().map(u -> u.getFilePath()).distinct().count();
        stats.totalMethods = allUnits.size();
        stats.securityRelevantMethods = (int) allScored.stream().filter(su -> su.finalScore > 0).count();
        stats.highPriorityMethods = top20Pct;
        stats.selectedForAi = selectedUnits.size();
        stats.contextExpandedCount = expandedUnits.size();
        
        long charsSelected = selectedUnits.stream().mapToLong(u -> u.getMethodBody() != null ? u.getMethodBody().length() : 0).sum() +
                             expandedUnits.stream().mapToLong(u -> u.getMethodBody() != null ? u.getMethodBody().length() : 0).sum();
        long charsTotal = allUnits.stream().mapToLong(u -> u.getMethodBody() != null ? u.getMethodBody().length() : 0).sum();
        stats.pctCodeReduction = charsTotal > 0 ? (1.0 - ((double) charsSelected / charsTotal)) : 0;
        
        return new com.devsecops.project_service.dto.SelectedCodeResponse(selectedUnits, expandedUnits, stats);
    }
}
