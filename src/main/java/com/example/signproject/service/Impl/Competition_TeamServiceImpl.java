package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import com.example.signproject.entity.Competition_Team;
import com.example.signproject.mapper.Competition_TeamMapper;
import com.example.signproject.service.CompetitionService;
import com.example.signproject.service.Competition_TeamService;
import com.example.signproject.service.TeamService;
import com.example.signproject.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Competition_TeamServiceImpl extends ServiceImpl<Competition_TeamMapper, Competition_Team> implements Competition_TeamService {

    private final CompetitionService competitionService;

    public Competition_TeamServiceImpl(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

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

    @Override
    public List<Competition_Team> getAllTeam(String competition) {
        return this.baseMapper.selectList(Wrappers.<Competition_Team>lambdaQuery().eq(Competition_Team::getCompetition, competition));
    }

    @Override
    public List<String> getAllCompetition(int team) {
        List<Competition_Team> competition_teams = this.baseMapper.selectList(Wrappers.<Competition_Team>lambdaQuery().eq(Competition_Team::getTeam, team));
        List<String> competitions = new ArrayList<>();
        for (Competition_Team competition_team :
                competition_teams) {
            competitions.add(competition_team.getCompetition());
        }
        return competitions;
    }
}
