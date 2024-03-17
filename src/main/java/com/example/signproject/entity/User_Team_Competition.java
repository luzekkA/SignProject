package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.signproject.Enum.RoutinesOptionalEnum;
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
    private RoutinesOptionalEnum type;
    private String groupp;
    private int sort;
    private String actions_scores;
    private String payment;
    private boolean pay;

}
