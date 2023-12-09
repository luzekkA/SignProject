package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;

public interface CompetitionService extends IService<Competition> {
    ResultJson<Object> CreateCompetition(Competition competition);

    ResultJson<Object> SelectAll();
}
