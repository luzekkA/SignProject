package com.example.signproject.controller;

import com.example.signproject.Utils.JwtUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import com.example.signproject.entity.Competition_Team;
import com.example.signproject.entity.User_Team_Competition;
import com.example.signproject.service.CompetitionService;
import com.example.signproject.service.Competition_TeamService;
import com.example.signproject.service.User_Team_CompetitionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("competition")
public class CompetitionController {
    private final CompetitionService competitionService;
    private final Competition_TeamService competition_teamService;
    private final User_Team_CompetitionService user_team_competitionService;

    public CompetitionController(CompetitionService competitionService, Competition_TeamService competitionTeamService, User_Team_CompetitionService userTeamCompetitionService) {
        this.competitionService = competitionService;
        competition_teamService = competitionTeamService;
        user_team_competitionService = userTeamCompetitionService;
    }

    @PostMapping("/create")
    public ResultJson<Object> Create(@RequestBody Competition competition) {
        return competitionService.CreateCompetition(competition);
    }

    @GetMapping("/selectAll")
    public ResultJson<Object> SelectAll() {
        return competitionService.SelectAll();
    }

    @PostMapping("/join")
    public ResultJson<Object> Join(@RequestBody Competition_Team competition_Team) {
        return competition_teamService.JoinCompetition(competition_Team);
    }

    //用户退出队伍
    @DeleteMapping("/quit")
    public ResultJson<Object> Quit(@RequestParam("competition") String competition, @RequestParam("team") int team) {
        Competition_Team competition_Team = new Competition_Team();
        competition_Team.setCompetition(competition);
        competition_Team.setTeam(team);
        return competition_teamService.QuitCompetition(competition_Team);
    }

    @PostMapping("/addScore")
    public ResultJson<Object> AddScore(@RequestBody User_Team_Competition user_team_competition) {
        return user_team_competitionService.addScore(user_team_competition);
    }

    @GetMapping("/getScore")
    public ResultJson<Object> GetScore(@RequestHeader("token") String token) {
        String id = JwtUtil.getId(token);
        return user_team_competitionService.getScore(id);
    }
}
