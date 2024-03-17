package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import com.example.signproject.entity.Competition_Team;

import java.util.List;

public interface Competition_TeamService extends IService<Competition_Team> {
    ResultJson<Object> JoinCompetition(Competition_Team competitionTeam);

    ResultJson<Object> QuitCompetition(Competition_Team competitionTeam);

    List<Competition_Team> getAllTeam(String competition);

    List<String> getAllCompetition(int team);
}
