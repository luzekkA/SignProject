package com.example.signproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.signproject.Enum.OtherRolesEnum;
import lombok.Data;

@Data
@TableName("other")
public class Other {
    private String name;
    private String phone;
    private OtherRolesEnum roles;
}
