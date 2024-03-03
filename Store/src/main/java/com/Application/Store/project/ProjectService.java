package com.Application.Store.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        System.out.println(filtered.size());
        return filtered;

    }
}
