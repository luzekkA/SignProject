package com.example.signproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.signproject.Utils.ResultJson;
import com.example.signproject.entity.Action_Code;
import org.springframework.stereotype.Service;

public interface Action_CodeService extends IService<Action_Code> {
    ResultJson<Object> findCode(String code, String type);
}
