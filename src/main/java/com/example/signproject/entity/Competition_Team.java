package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("competition_team")
public class Competition_Team {
    private String competition;
    private int team;
}
