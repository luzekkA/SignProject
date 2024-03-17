package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.signproject.Enum.OtherRolesEnum;
import lombok.Data;

@Data
@TableName("team_other")
public class Team_Other {
    private int team;
    private String name;
    private String phone;
    private OtherRolesEnum role;
}
