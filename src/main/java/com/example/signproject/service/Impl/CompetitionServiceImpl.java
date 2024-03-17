package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.FileUtil;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Competition;
import com.example.signproject.mapper.CompetitionMapper;
import com.example.signproject.service.CompetitionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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
            for (Competition competition : competitionList) {
                competition.setInformation(FileUtil.getObjectUrl(competition.getInformation()));
            }
            return ResultJson.success(competitionList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("全部查询出现异常");
            return ResultJson.error(log.toString());
        }
    }

    @Override
    public ResultJson<Object> UploadCompetitionInfo(String name, MultipartFile file) {
        Competition competition = this.getOne(
                Wrappers.<Competition>lambdaQuery()
                        .eq(Competition::getName, name)
        );
        if (FileUtil.isPdf(file).getCode() == 20000 && competition != null) {
            String filename = name
                    + Objects.requireNonNull(file
                            .getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            ResultJson<Object> resultJson = FileUtil.uploadFileToMinio(file, filename);
            if (resultJson.getCode() == 20000) {
                competition.setInformation(filename);
            } else {
                return resultJson;
            }
            if (this.updateById(competition)) {
                return ResultJson.success();
            }
            return ResultJson.error("数据库信息更新失败");
        }
        return ResultJson.error("文件为空");
    }
}
