package com.example.signproject.controller;

import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.JoinTeamViewModel;
import com.example.signproject.entity.Team;
import com.example.signproject.service.TeamService;
import com.example.signproject.service.Team_UserService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@RequestMapping("team")
public class TeamController {
    private final TeamService teamService;
    private final Team_UserService team_userService;

    public TeamController(TeamService teamService, Team_UserService teamUserService) {
        this.teamService = teamService;
        team_userService = teamUserService;
    }

    @PostMapping("/create")
    public ResultJson<Object> TeamCreate(@RequestBody Team team) {
        return this.teamService.create(team);
    }

    @GetMapping("/all")
    public ResultJson<Object> AllTeam(String leader) {
        return this.teamService.findByLeader(leader);
    }

    @DeleteMapping("/delete")
    public ResultJson<Object> DeleteTeam(@RequestParam("id") Serializable id) {
        return this.teamService.delete(id);
    }

    @PostMapping("/join")
    public ResultJson<Object> JoinTeam(@RequestBody JoinTeamViewModel joinTeamViewModel) {
        return team_userService.JoinTeam(joinTeamViewModel);
    }

    @GetMapping("/getteam")
    public ResultJson<Object> GetTeam(@RequestHeader("token") String token) {
        //String token = request.getHeader("token");
        String id = JwtUtil.getId(token);
        return this.team_userService.GetTeamByUserId(id);
    }

    @DeleteMapping("/quitteam")
    public ResultJson<Object> QuitTeam(@RequestParam("team") int team, @RequestHeader("token") String token) {
        //String token = request.getHeader("token");
        String id = JwtUtil.getId(token);
        return this.team_userService.QuitTeamByUserId(id, team);
    }

    @GetMapping("/getuser")
    public ResultJson<Object> GetUser(@RequestParam("team") int team) {
        return this.team_userService.GetUserByTeamId(team);
    }
}
