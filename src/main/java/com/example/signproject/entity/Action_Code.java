package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("action_code")
public class Action_Code {
    private String code;
    private String type;
    private String action;
}
