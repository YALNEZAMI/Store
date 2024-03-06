package com.Application.Store.team;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("")
    public Team create(@RequestBody Team task) {
        return this.teamService.create(task);
    }

    @PostMapping("/uploadTeamPhoto")
    public Team uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("teamId") String teamId) {
        Team task = this.teamService.uploadTeamPhoto(file, teamId);
        return task;
    }

    @GetMapping("/withMemberId/{memberId}")
    public List<Team> getWithMemberId(@PathVariable String memberId) {
        return this.teamService.getWithMemberId(memberId);
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable String id) {
        return this.teamService.getTeamById(id);
    }

    @DeleteMapping("/deleteMember/{teamId}/{memberId}")
    public Team deleteMember(@PathVariable String teamId, @PathVariable String memberId) {
        return this.teamService.deleteMember(teamId, memberId);
    }

    @DeleteMapping("/{teamId}")
    public void deleteTeam(@PathVariable String teamId) {
        this.teamService.deleteTeam(teamId);
    }

    @GetMapping("/addMember/{teamId}/{memberId}")
    public Team addMember(@PathVariable String teamId, @PathVariable String memberId) {
        return this.teamService.addMember(teamId, memberId);
    }
}