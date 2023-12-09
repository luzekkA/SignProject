package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition_Team;
import com.example.signproject.mapper.Competition_TeamMapper;
import com.example.signproject.service.Competition_TeamService;
import org.springframework.stereotype.Service;

@Service
public class Competition_TeamServiceImpl extends ServiceImpl<Competition_TeamMapper, Competition_Team> implements Competition_TeamService {
    @Override
    public ResultJson<Object> JoinCompetition(Competition_Team competitionTeam) {
        try {
            if (this.baseMapper.insert(competitionTeam) != -1) {
                return ResultJson.success();
            }
            return ResultJson.error("插入失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> QuitCompetition(Competition_Team competitionTeam) {
        try {
            if (this.baseMapper.delete(Wrappers
                    .<Competition_Team>lambdaQuery()
                    .eq(Competition_Team::getCompetition, competitionTeam.getCompetition())
                    .eq(Competition_Team::getTeam, competitionTeam.getTeam())) != 0) {
                return ResultJson.success();
            }
            return ResultJson.error("退出失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }
}
