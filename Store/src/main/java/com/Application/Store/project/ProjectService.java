package com.Application.Store.project;

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

import com.Application.Store.user.User;

@Service
public class ProjectService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    ProjectRepo projectRepo;

    public Project create(Project project) {
        return this.projectRepo.save(project);
    }

    public List<Project> getParticipantProject(String userId) {
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

    public Project uploadProjectPhoto(MultipartFile file, String projectId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            String UPLOAD_FOLDER = "./Store/src/main/resources/static/projectPhoto/"; // Change this to
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
            projectToUpdate.setProjectPhoto(this.API_URL + "/projectPhoto/" + projectId + ext);
            this.projectRepo.save(projectToUpdate);
            return projectToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
