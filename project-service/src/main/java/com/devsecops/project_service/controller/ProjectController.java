package com.devsecops.project_service.controller;


import com.devsecops.project_service.entity.Project;
import com.devsecops.project_service.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Project> getProjectsByUser(
            @PathVariable Long userId) {

        return projectService.getProjectsByUser(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/{id}/upload")
    public void uploadProject(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        projectService.uploadAndExtractProject(id, file);
    }
}
