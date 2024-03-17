package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.FileUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.User_Team_Competition;
import com.example.signproject.mapper.User_Team_CompetitionMapper;
import com.example.signproject.service.User_Team_CompetitionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class User_Team_CompetitionServiceImpl extends ServiceImpl<User_Team_CompetitionMapper, User_Team_Competition> implements User_Team_CompetitionService {
    @Override
    public ResultJson<Object> addScore(User_Team_Competition user_team_competition) {
        try {
            if (exist(user_team_competition)) {
                if (this.update(user_team_competition, Wrappers.<User_Team_Competition>lambdaUpdate()
                        .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                        .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                        .eq(User_Team_Competition::getTeam, user_team_competition.getTeam())
                        .set(User_Team_Competition::getScore, user_team_competition.getScore()))) {
                    return ResultJson.success();
                }
            } else {
                if (this.save(user_team_competition)) {
                    return ResultJson.success();
                }
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

    @Override
    public ResultJson<Object> set_TeamAndType(User_Team_Competition user_team_competition) {
        try {
            if (!this.baseMapper.exists(Wrappers.<User_Team_Competition>lambdaQuery()
                    .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                    .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                    .eq(User_Team_Competition::getTeam, user_team_competition.getTeam()))) {
                if (create(user_team_competition)) {
                    return ResultJson.success();
                } else {
                    return ResultJson.error("插入失败");
                }
            } else if (this.update(user_team_competition, Wrappers.<User_Team_Competition>lambdaUpdate()
                    .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                    .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                    .eq(User_Team_Competition::getTeam, user_team_competition.getTeam())
                    .set(User_Team_Competition::getType, user_team_competition.getType())
                    .set(User_Team_Competition::getGroupp, user_team_competition.getGroupp()))) {
                return ResultJson.success();
            }
            return ResultJson.error("修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public List<User_Team_Competition> exportSheet(String competition) {
        List<User_Team_Competition> user_team_competitionList = this.baseMapper.selectList(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getCompetition, competition)
                //.eq(User_Team_Competition::isPay, true)
        );
        user_team_competitionList = sort(user_team_competitionList);
        boolean flag = true;
        for (User_Team_Competition user_team_competition : user_team_competitionList) {
            if (!this.update(user_team_competition, Wrappers.<User_Team_Competition>lambdaUpdate()
                    .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                    .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                    .eq(User_Team_Competition::getTeam, user_team_competition.getTeam())
                    .set(User_Team_Competition::getSort, user_team_competition.getSort()))) {
                flag = false;
            }
        }
        return flag ? user_team_competitionList : null;
    }

    @Override
    public ResultJson<Object> setActions_scores(User_Team_Competition user_team_competition) {
        if (this.update(user_team_competition, Wrappers.<User_Team_Competition>lambdaUpdate()
                .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                .eq(User_Team_Competition::getTeam, user_team_competition.getTeam())
                .set(User_Team_Competition::getActions_scores, user_team_competition.getActions_scores()))) {
            return ResultJson.success();
        }
        return ResultJson.error("修改失败");
    }

    @Override
    public ResultJson<Object> getActions_scores(String user, String competition, int team) {
        User_Team_Competition user_team_competition = this.getOne(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser, user)
                .eq(User_Team_Competition::getCompetition, competition)
                .eq(User_Team_Competition::getTeam, team));
        if (user_team_competition != null) {
            return ResultJson.success(user_team_competition.getActions_scores());
        }
        return ResultJson.error("查询失败");
    }

    @Override
    public ResultJson<Object> getPayments(String competition) {
        List<User_Team_Competition> list= this.baseMapper.selectList(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getCompetition, competition)
        );
        for (User_Team_Competition user_team_competition : list) {
            user_team_competition.setPayment(FileUtil.getObjectUrl(user_team_competition.getPayment()));
        }
        return !list.isEmpty() ? ResultJson.success(list) : ResultJson.error("查询失败");
    }

    @Override
    public ResultJson<Object> sureIsPay(String competition, String user, int team) {
        return this.update(Wrappers.<User_Team_Competition>lambdaUpdate()
                .eq(User_Team_Competition::getCompetition, competition)
                .eq(User_Team_Competition::getUser, user)
                .eq(User_Team_Competition::getTeam, team)
                .set(User_Team_Competition::isPay, true)) ? ResultJson.success() : ResultJson.error("确认失败");
    }

    @Override
    public ResultJson<Object> getAllActions_scores(String user, int team) {
        List<User_Team_Competition> user_team_competitionList = this.baseMapper.selectList(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser, user)
                .eq(User_Team_Competition::getTeam, team));
        return !user_team_competitionList.isEmpty() ? ResultJson.success(user_team_competitionList) : ResultJson.success(null);
    }

    public boolean exist(User_Team_Competition user_team_competition) {
        return this.getOne(Wrappers.<User_Team_Competition>lambdaQuery()
                .eq(User_Team_Competition::getUser, user_team_competition.getUser())
                .eq(User_Team_Competition::getCompetition, user_team_competition.getCompetition())
                .eq(User_Team_Competition::getTeam, user_team_competition.getTeam())) != null;
    }

    public boolean create(User_Team_Competition user_team_competition) {
        return this.baseMapper.insert(user_team_competition) != -1;
    }

    public List<User_Team_Competition> sort(List<User_Team_Competition> user_team_competitionList) {
        List<User_Team_Competition> result = new ArrayList<>();
        List<User_Team_Competition> temp = new ArrayList<>();
        for (String group : new String[]{"甲", "乙", "丙", "丁"}) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 2; j++) {
                    for (User_Team_Competition user_team_competition : user_team_competitionList) {
                        if (user_team_competition.getGroupp().equals(group)
                                && user_team_competition.getType().getKey() == i
                                && (int) user_team_competition.getUser().charAt(17) % 2 == j) {

                            temp.add(user_team_competition);
                        }
                    }
                    Collections.shuffle(temp);
                    for (int k = 0; k < temp.size(); k++) {
                        temp.get(k).setSort(k + 1);
                    }
                    result.addAll(temp);
                    temp.clear();
                }
            }
        }
        return result;
    }
}
