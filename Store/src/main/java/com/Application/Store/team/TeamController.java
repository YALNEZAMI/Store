package com.Application.Store.team;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.Application.Store.project.Project;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;

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

}