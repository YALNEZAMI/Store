package com.Application.Toog.team;

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

import com.Application.Toog.project.Project;
import com.Application.Toog.user.User;
import com.Application.Toog.user.UserService;

@Service
public class TeamService {
    @Value("${API_URL}")
    private String API_URL;

    @Autowired
    TeamRepo teamRepo;
    @Autowired
    UserService userService;
    String UPLOAD_FOLDER = "./Toog/src/main/resources/static/teamPhoto/"; // Change this to

    public Team create(Team task) {
        return this.teamRepo.save(task);
    }

    public Team uploadTeamPhoto(MultipartFile file, String teamId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // upload the file to the profile photo folder
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
            // delete the old file
            this.deletePhoto(teamToUpdate);
            teamToUpdate.setTeamPhoto(this.API_URL + "/teamPhoto/" + teamId + ext);
            this.teamRepo.save(teamToUpdate);
            return teamToUpdate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePhoto(Team team) {
        String oldPhotoUrl = team.getPhoto();
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

    public List<Team> getWithOwnerId(String memberId) {
        List<Team> teams = this.teamRepo.findAll();
        List<Team> result = new ArrayList<>();
        for (Team team : teams) {
            User owner = team.getOwner();
            if (owner.getId().equals(memberId)) {
                result.add(team);

            }
        }
        return result;
    }

    public Team getTeamById(String id) {
        Team team = this.teamRepo.findById(id).orElse(null);
        // populate owner
        User owner = this.userService.getUser(team.getOwner().getId());
        team.setOwner(owner);
        // populate members
        List<User> members = new ArrayList<>();
        for (User member : team.getMembers()) {
            members.add(this.userService.getUser(member.getId()));
        }
        team.setMembers(members);
        return team;
    }

    public Team deleteMember(String teamId, String memberId) {
        Team team = this.getTeamById(teamId);
        // make new members list without the given member to delete
        List<User> newMembers = new ArrayList<>();
        for (User user : team.getMembers()) {
            if (!user.getId().equals(memberId)) {
                newMembers.add(user);
            }
        }
        // set the new members list to the team
        team.setMembers(newMembers);
        // update the team in database
        this.teamRepo.save(team);
        return team;
    }

    public Team addMember(String teamId, String memberId) {
        Team team = this.getTeamById(teamId);
        // check if member already exist
        for (User member : team.getMembers()) {
            if (member.getId().equals(memberId)) {
                return team;
            }
        }
        // retrieve member from data base
        User member = this.userService.getUser(memberId);
        // add member to the team member list
        team.getMembers().add(member);
        // set the new list to the team
        team.setMembers(team.getMembers());
        // update in database
        this.teamRepo.save(team);
        return team;
    }

    public void deleteTeam(String teamID) {
        System.out.println(11111111);
        Team team = this.getTeamById(teamID);
        this.deletePhoto(team);
        this.teamRepo.deleteById(teamID);
    }

    public void deleteTeamsOfOwner(String ownerId) {
        List<Team> teams = this.teamRepo.findAll();
        for (Team team : teams) {
            if (team.getOwner().getId().equals(ownerId)) {
                this.deleteTeam(team.getId());
            }
        }
    }

}
