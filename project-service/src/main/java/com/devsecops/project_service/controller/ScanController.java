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
}
