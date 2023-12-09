package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.User_Team_Competition;

public interface User_Team_CompetitionService extends IService<User_Team_Competition> {
    ResultJson<Object> addScore(User_Team_Competition user_team_competition);

    ResultJson<Object> getScore(String id);
}
