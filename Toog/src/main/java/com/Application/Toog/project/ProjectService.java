package com.Application.Toog.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Application.Toog.task.TaskService;
import com.Application.Toog.user.User;
import com.Application.Toog.user.UserService;

@Service
public class ProjectService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    String UPLOAD_FOLDER = "./Toog/src/main/resources/static/projectPhoto/"; // Change this to

    public Project create(Project project) {
        return this.projectRepo.save(project);
    }

    public List<Project> getParticipantProjects(String userId) {
        List<Project> allProjects = this.projectRepo.findAll();
        List<Project> filtered = new ArrayList<Project>();
        for (Project project : allProjects) {
            for (User user : project.getParticipants()) {
                if (user.getId().equals(userId)) {
                    filtered.add(project);
                }
            }
        }
        return filtered;

    }

    public List<Project> getOwnerProjects(String userId) {
        List<Project> allProjects = this.projectRepo.findAll();
        List<Project> filtered = new ArrayList<Project>();
        for (Project project : allProjects) {
            User owner = project.getOwner();
            if (owner.getId().equals(userId)) {
                filtered.add(project);

            }
        }
        return filtered;

    }

    public Project uploadProjectPhoto(MultipartFile file, String projectId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            Path directory = Paths.get(UPLOAD_FOLDER);
            // create a direction if not exist
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            // treatement oo fthe file name
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            String ext = "." + split[split.length - 1];
            // uploading
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + projectId + ext);
            Files.write(path, bytes);
            // update the user photo url in the data base
            Project projectToUpdate = this.projectRepo.findById(projectId).orElse(null);
            // delete the old file
            this.deletePhoto(projectToUpdate);
            projectToUpdate.setProjectPhoto(this.API_URL + "/projectPhoto/" + projectId + ext);
            this.projectRepo.save(projectToUpdate);
            return projectToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePhoto(Project project) {
        String oldPhotoUrl = project.getPhoto();
        String[] oldPhotoUrlSplit = oldPhotoUrl.split("/");
        String oldPhotoPath = UPLOAD_FOLDER + oldPhotoUrlSplit[oldPhotoUrlSplit.length - 1];
        File fileToDelete = new File(oldPhotoPath);
        // Check if the file exists before attempting to delete it
        if (fileToDelete.exists()) {
            // Attempt to delete the file
            boolean deleted = fileToDelete.delete();

            // Check if the deletion was successful
            if (deleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    public Project getProjectById(String id) {
        Project project = this.projectRepo.findById(id).orElse(null);
        // fill owner
        User owner = this.userService.getUser(project.getOwner().getId());
        project.setOwner(owner);
        // fill participants
        ArrayList<User> participants = new ArrayList<>();
        for (User participant : project.getParticipants()) {
            participants.add(this.userService.getUser(participant.getId()));

        }
        project.setParticipants(participants);
        return project;
    }

    public void deleteProject(String projectId) {
        // delete all related tasks
        this.taskService.deleteProjectTasks(projectId);
        // delete project photo
        Project project = this.getProjectById(projectId);
        this.deletePhoto(project);
        // delete project from database
        this.projectRepo.deleteById(projectId);

    }

    public void deleteProjectsOfUser(String userId) {
        List<Project> projects = this.projectRepo.findAll();
        for (Project project : projects) {
            if (project.getOwner().getId().equals(userId)) {
                this.deleteProject(project.getId());
            }
        }
    }
}
