package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Team_Other;
import com.example.signproject.mapper.Team_OtherMapper;
import com.example.signproject.service.Team_OtherService;
import org.springframework.stereotype.Service;

@Service
public class Team_OtherServiceImpl extends ServiceImpl<Team_OtherMapper, Team_Other> implements Team_OtherService {
    @Override
    public ResultJson<Object> add(Team_Other team_other) {
        try {
            int flag = this.baseMapper.insert(team_other);
            if (flag != -1) {
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
    public ResultJson<Object> showAll(int team) {
        try {
            return ResultJson.success(this.baseMapper.selectList(Wrappers.<Team_Other>lambdaQuery().eq(Team_Other::getTeam, team)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> delete(int team, String phone) {
        try {
            return ResultJson.success(this.baseMapper.delete(Wrappers.<Team_Other>lambdaQuery().eq(Team_Other::getTeam, team).eq(Team_Other::getPhone, phone)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除出现异常");
            return ResultJson.error(log.toString());
        }
    }
}
