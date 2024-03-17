package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.InvitationUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Team;
import com.example.signproject.mapper.TeamMapper;
import com.example.signproject.service.TeamService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
    @Override
    public ResultJson<Object> create(Team team) {
        try {
            if (team.getName() != null && team.getLeader() != null) {
                String invitation = InvitationUtil.CreateCode(team.getName() + team.getLeader() + LocalDateTime.now());
                team.setInvitation(invitation);
                int id = this.baseMapper.insert(team);
                if (id == -1) {
                    return ResultJson.error("创建失败");
                } else {
                    Team temp = findByInvitation(invitation);
                    return ResultJson.success(temp.getId());
                }
            }
            return ResultJson.error("队伍信息丢失");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> findByLeader(String leader) {
        try {
            List<Team> teamList = this.baseMapper.selectList(
                    Wrappers.<Team>lambdaQuery()
                            .eq(Team::getLeader, leader)
            );
            return ResultJson.success(teamList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> delete(Serializable id) {
        try {
            if (this.baseMapper.deleteById(id) != 0) {
                return ResultJson.success();
            }
            return ResultJson.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public Team findByInvitation(String invitation) {
        return this.baseMapper.selectOne(Wrappers.<Team>lambdaQuery().eq(Team::getInvitation, invitation));
    }
}
