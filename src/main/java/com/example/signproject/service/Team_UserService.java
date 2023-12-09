package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.JoinTeamViewModel;
import com.example.signproject.entity.Team_User;

public interface Team_UserService extends IService<Team_User> {
    ResultJson<Object> JoinTeam(JoinTeamViewModel joinTeamViewModel);

    ResultJson<Object> GetTeamByUserId(String id);

    ResultJson<Object> QuitTeamByUserId(String id, int teamId);
}
