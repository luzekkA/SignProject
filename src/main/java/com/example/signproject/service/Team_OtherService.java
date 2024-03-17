package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Team_Other;

public interface Team_OtherService extends IService<Team_Other> {
    ResultJson<Object> add(Team_Other team_other);

    ResultJson<Object> showAll(int team);

    ResultJson<Object> delete(int team, String phone);
}
