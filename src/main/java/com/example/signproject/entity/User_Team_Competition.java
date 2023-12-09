package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("user_team_competition")
@AllArgsConstructor
public class User_Team_Competition {
    private String user;
    private int team;
    private String competition;
    private double score;
}
