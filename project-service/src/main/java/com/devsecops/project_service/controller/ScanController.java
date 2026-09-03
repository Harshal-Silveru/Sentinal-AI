package com.devsecops.project_service.controller;

import com.devsecops.project_service.entity.Scan;
import com.devsecops.project_service.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scan createScan(@RequestBody Scan scan) {
        return scanService.createScan(scan);
    }

    @GetMapping
    public List<Scan> getAllScans() {
        return scanService.getAllScans();
    }

    @GetMapping("/{id}")
    public Scan getScan(@PathVariable Long id) {
        return scanService.getScanById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<Scan> getScansByProject(
            @PathVariable Long projectId) {

        return scanService.getScansByProject(projectId);
    }

    @GetMapping("/{id}/findings")
    public List<com.devsecops.project_service.entity.Finding> getFindingsByScanId(@PathVariable Long id) {
        return scanService.getFindingsByScanId(id);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.devsecops.project_service.srcs.SrcsScorer srcsScorer;
    
    @org.springframework.beans.factory.annotation.Autowired
    private com.devsecops.project_service.srcs.ContextExpander contextExpander;

    @GetMapping("/{id}/selected-code")
    public com.devsecops.project_service.dto.SelectedCodeResponse getSelectedCode(@PathVariable Long id) throws Exception {
        return scanService.getSelectedCodeForScan(id, srcsScorer, contextExpander);
    }
}
