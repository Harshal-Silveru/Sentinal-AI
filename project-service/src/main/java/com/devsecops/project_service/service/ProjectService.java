package com.devsecops.project_service.service;

import com.devsecops.project_service.client.UserServiceClient;
import com.devsecops.project_service.dto.UserResponse;
import com.devsecops.project_service.entity.Project;
import com.devsecops.project_service.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserServiceClient userServiceClient;

    public ProjectService(ProjectRepository projectRepository,
                          UserServiceClient userServiceClient) {

        this.projectRepository = projectRepository;
        this.userServiceClient = userServiceClient;
    }

    public Project createProject(Project project) {

        // Verify user exists in User Service
        UserResponse user =
                userServiceClient.getUserById(project.getUserId());

        if (user == null) {
            throw new RuntimeException("User does not exist");
        }

        project.setUploadDate(LocalDateTime.now());

        if (project.getStatus() == null ||
                project.getStatus().isBlank()) {

            project.setStatus("UPLOADED");
        }

        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));
    }

    public List<Project> getProjectsByUser(Long userId) {

        return projectRepository.findByUserId(userId);
    }

    public void deleteProject(Long id) {

        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }

        projectRepository.deleteById(id);
    }
}
