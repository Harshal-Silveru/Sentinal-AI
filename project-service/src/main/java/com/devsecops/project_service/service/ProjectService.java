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

    public void uploadAndExtractProject(Long projectId, org.springframework.web.multipart.MultipartFile file) {
        Project project = getProjectById(projectId);

        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            throw new IllegalArgumentException("Invalid file format. Only ZIP files are accepted.");
        }

        if (file.getSize() > 20 * 1024 * 1024) { // 20MB
            throw new IllegalArgumentException("File size exceeds 20MB limit");
        }

        try {
            java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("project_extract_" + projectId);
            try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(file.getInputStream())) {
                java.util.zip.ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    java.nio.file.Path newPath = zipSlipProtect(zipEntry, tempDir);
                    if (zipEntry.isDirectory()) {
                        java.nio.file.Files.createDirectories(newPath);
                    } else {
                        if (newPath.getParent() != null) {
                            if (java.nio.file.Files.notExists(newPath.getParent())) {
                                java.nio.file.Files.createDirectories(newPath.getParent());
                            }
                        }
                        java.nio.file.Files.copy(zis, newPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                    zipEntry = zis.getNextEntry();
                }
                zis.closeEntry();
            }

            project.setExtractionPath(tempDir.toAbsolutePath().toString());
            project.setStatus("EXTRACTED");
            projectRepository.save(project);

            org.slf4j.LoggerFactory.getLogger(ProjectService.class).info("Extraction complete for project {} at {}", projectId, tempDir);

        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to extract zip file", e);
        }
    }

    private java.nio.file.Path zipSlipProtect(java.util.zip.ZipEntry zipEntry, java.nio.file.Path targetDir) throws java.io.IOException {
        java.nio.file.Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        java.nio.file.Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new java.io.IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }
}
