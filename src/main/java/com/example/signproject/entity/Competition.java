package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("competition")
public class Competition {
    @TableId
    private String name;
    private String information;
    private boolean state;
}
