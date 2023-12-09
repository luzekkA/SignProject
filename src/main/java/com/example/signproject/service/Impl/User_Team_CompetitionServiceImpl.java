package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.User_Team_Competition;
import com.example.signproject.mapper.User_Team_CompetitionMapper;
import com.example.signproject.service.User_Team_CompetitionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class User_Team_CompetitionServiceImpl extends ServiceImpl<User_Team_CompetitionMapper, User_Team_Competition> implements User_Team_CompetitionService {
    @Override
    public ResultJson<Object> addScore(User_Team_Competition user_team_competition) {
        try {
            if (this.save(user_team_competition)) {
                return ResultJson.success();
            }
            return ResultJson.error("插入成绩失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> getScore(String id) {
        try {
            List<User_Team_Competition> user_team_competitionList = this.list(Wrappers.<User_Team_Competition>lambdaQuery().eq(User_Team_Competition::getUser, id));
            if (user_team_competitionList != null) {
                return ResultJson.success(user_team_competitionList);
            }
            return ResultJson.error("无成绩");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }
}
