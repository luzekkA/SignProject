package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.JoinTeamViewModel;
import com.example.signproject.entity.Team;
import com.example.signproject.entity.Team_User;
import com.example.signproject.mapper.Team_UserMapper;
import com.example.signproject.service.TeamService;
import com.example.signproject.service.Team_UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Team_UserServiceImpl extends ServiceImpl<Team_UserMapper, Team_User> implements Team_UserService {
    private final TeamService teamService;

    public Team_UserServiceImpl(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public ResultJson<Object> JoinTeam(JoinTeamViewModel joinTeamViewModel) {
        try {
            Team_User team_user = new Team_User();
            team_user.setUser(joinTeamViewModel.getUserId());
            String invitation = joinTeamViewModel.getInvitation();
            Team team = teamService.findByInvitation(invitation);
            team_user.setTeam(team.getId());
            if (this.baseMapper.insert(team_user) != -1) {
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
    public ResultJson<Object> GetTeamByUserId(String id) {
        try {
            List<Team> teamList = new ArrayList<>();
            List<Team_User> teamUserList = this.list(Wrappers.<Team_User>lambdaQuery().eq(Team_User::getUser, id).select(Team_User::getTeam));
            List<Team> team = teamService.list(Wrappers.<Team>lambdaQuery().eq(Team::getLeader, id));
            for (Team_User teamId :
                    teamUserList) {
                teamList.add(teamService.getById(teamId.getTeam()));
            }
            for (Team teamId :
                    team) {
                teamList.add(teamService.getById(teamId.getId()));
            }
            return ResultJson.success(teamList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> QuitTeamByUserId(String id, int teamId) {
        try {
            if (this.remove(Wrappers.<Team_User>lambdaQuery().eq(Team_User::getUser, id).eq(Team_User::getTeam, teamId))) {
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
