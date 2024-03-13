package com.Application.Toog.project;

import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Project create(@RequestBody Project project) {
        return this.projectService.create(project);
    }

    @GetMapping("withParticipant/{userId}")
    public List<Project> getParticipantProjects(@PathVariable String userId) {
        return this.projectService.getParticipantProjects(userId);
    }

    @GetMapping("withOwner/{userId}")
    public List<Project> getOwnerProjects(@PathVariable String userId) {
        return this.projectService.getOwnerProjects(userId);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable String id) {
        return this.projectService.getProjectById(id);
    }

    @PostMapping("/uploadProjectPhoto")
    public Project uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("projectId") String projectId) {
        Project project = this.projectService.uploadProjectPhoto(file, projectId);
        return project;
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId) {
        this.projectService.deleteProject(projectId);
    }

    @DeleteMapping("deleteProjectsOfUser/{userId}")
    public void deleteProjectsOfUser(@PathVariable String userId) {
        this.projectService.deleteProjectsOfUser(userId);
    }
}