package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.User_Team_Competition;

import java.util.List;

public interface User_Team_CompetitionService extends IService<User_Team_Competition> {
    ResultJson<Object> addScore(User_Team_Competition user_team_competition);

    ResultJson<Object> getScore(String id);

    ResultJson<Object> set_TeamAndType(User_Team_Competition user_team_competition);

    List<User_Team_Competition> exportSheet(String competition);
    ResultJson<Object> setActions_scores(User_Team_Competition user_team_competition);
    ResultJson<Object> getActions_scores(String user, String competition, int team);
    ResultJson<Object> getPayments(String competition);
    ResultJson<Object> sureIsPay(String competition ,String user, int team);
    ResultJson<Object> getAllActions_scores(String user, int team);
}
