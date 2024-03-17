package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.ViewModel.GetTeamUserViewModel;
import com.example.signproject.ViewModel.JoinTeamViewModel;
import com.example.signproject.entity.Team;
import com.example.signproject.entity.Team_User;
import com.example.signproject.entity.User;
import com.example.signproject.mapper.Team_UserMapper;
import com.example.signproject.service.TeamService;
import com.example.signproject.service.Team_UserService;
import com.example.signproject.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Team_UserServiceImpl extends ServiceImpl<Team_UserMapper, Team_User> implements Team_UserService {
    private final TeamService teamService;
    private final UserService userService;

    public Team_UserServiceImpl(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
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

    @Override
    public ResultJson<Object> GetUserByTeamId(int teamId) {
        try {
            List<Team_User> teamUserList = this.list(Wrappers.<Team_User>lambdaQuery().eq(Team_User::getTeam, teamId).select(Team_User::getUser));
            Team team = teamService.getById(teamId);
            List<GetTeamUserViewModel> getTeamUserViewModelList = new ArrayList<>();
            if (team != null) {
//                User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,team.getLeader()).eq(User::getRoles,"leader"));
//                getTeamUserViewModelList.add(new GetTeamUserViewModel(user.getId(), team.getLeader(),"leader"));
                for (Team_User teamUser :
                        teamUserList) {
                    User user = userService.getById(teamUser.getUser());
                    getTeamUserViewModelList.add(new GetTeamUserViewModel(teamUser.getUser(), user.getUsername(), user.getRoles().toString(), user.getPhone()));
                }
                return ResultJson.success(getTeamUserViewModelList);
            }
            return ResultJson.error("查询失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public List<Team_User> getAll(int team) {
        return this.baseMapper.selectList(Wrappers.<Team_User>lambdaQuery().eq(Team_User::getTeam, team));
    }

    @Override
    public ResultJson<Object> DeleteUser(int team, String user) {
        try {
            return ResultJson.success(this.baseMapper.delete(Wrappers.<Team_User>lambdaQuery().eq(Team_User::getTeam, team).eq(Team_User::getUser, user)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除出现异常");
            return ResultJson.error(log.toString());
        }
    }
}
