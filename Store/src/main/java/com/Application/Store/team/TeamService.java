package com.Application.Store.team;

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

import com.Application.Store.project.Project;
import com.Application.Store.user.User;

@Service
public class TeamService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    TeamRepo teamRepo;

    public Team create(Team task) {
        return this.teamRepo.save(task);
    }

    public Team uploadTeamPhoto(MultipartFile file, String teamId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
            String UPLOAD_FOLDER = "./Store/src/main/resources/static/teamPhoto/"; // Change this to
            Path directory = Paths.get(UPLOAD_FOLDER);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            String ext = "." + split[split.length - 1];

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + teamId + ext);
            Files.write(path, bytes);
            // update the user photo url in the data base
            Team teamToUpdate = this.teamRepo.findById(teamId).orElse(null);
            teamToUpdate.setTeamPhoto(this.API_URL + "/teamPhoto/" + teamId + ext);
            this.teamRepo.save(teamToUpdate);
            return teamToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Team> getWithMemberId(String memberId) {
        List<Team> teams = this.teamRepo.findAll();
        List<Team> result = new ArrayList<>();
        for (Team team : teams) {
            for (User user : team.getMembers()) {
                if (user.getId().equals(memberId)) {
                    result.add(team);
                }
            }
        }
        return result;
    }
}
