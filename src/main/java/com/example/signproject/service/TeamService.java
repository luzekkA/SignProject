package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Team;

import java.io.Serializable;

public interface TeamService extends IService<Team> {
    ResultJson<Object> create(Team team);

    ResultJson<Object> findByLeader(String leader);

    ResultJson<Object> delete(Serializable id);

    Team findByInvitation(String invitation);
}
