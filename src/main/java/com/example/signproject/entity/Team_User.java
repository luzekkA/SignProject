package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("team_user")
public class Team_User {
    private int team;
    private String user;
}
