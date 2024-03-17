package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import org.springframework.web.multipart.MultipartFile;

public interface CompetitionService extends IService<Competition> {
    ResultJson<Object> CreateCompetition(Competition competition);

    ResultJson<Object> SelectAll();

    ResultJson<Object> UploadCompetitionInfo(String name, MultipartFile file);
}
