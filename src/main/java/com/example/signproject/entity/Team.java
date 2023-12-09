package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("team")
public class Team {
    @TableId
    private int id;
    private String name;
    private String leader;
    private String invitation;
}
