package com.example.signproject.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Action_Code;
import com.example.signproject.mapper.Action_CodeMapper;
import com.example.signproject.service.Action_CodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Action_CodeServiceImpl extends ServiceImpl<Action_CodeMapper, Action_Code> implements Action_CodeService {
    @Override
    public ResultJson<Object> findCode(String code, String type) {
        try {
            List<Action_Code> action_codeList = this.baseMapper.selectList(Wrappers.<Action_Code>lambdaQuery().eq(Action_Code::getType, type).like(Action_Code::getCode, code));
            if (action_codeList != null) {
                return ResultJson.success(action_codeList);
            } else {
                return ResultJson.success(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.error("查询失败");
        }
    }
}
