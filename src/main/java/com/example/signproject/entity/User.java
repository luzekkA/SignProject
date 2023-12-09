package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.signproject.Utils.Roles;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId
    private String id;

    private String username;

    private String password;

    private String phone;

    private Roles roles;

    private String ico;
}