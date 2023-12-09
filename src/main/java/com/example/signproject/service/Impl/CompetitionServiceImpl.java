package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import com.example.signproject.mapper.CompetitionMapper;
import com.example.signproject.service.CompetitionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapper, Competition> implements CompetitionService {

    @Override
    public ResultJson<Object> CreateCompetition(Competition competition) {
        try {
            if (competition.getName() != null && competition.getInformation() != null) {
                int id = this.baseMapper.insert(competition);
                if (id == -1) {
                    return ResultJson.error("创建失败");
                } else {
                    return ResultJson.success();
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
    public ResultJson<Object> SelectAll() {
        try {
            List<Competition> competitionList = this.baseMapper.selectList(null);
            return ResultJson.success(competitionList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }
}
