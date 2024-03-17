package com.example.signproject.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoutinesOptionalEnum {
    LongBoxing(0, "长拳"),
    SouthernBoxing(1, "南拳"),
    TaiChi(2, "太极拳"),
    Swordsmanship(3, "剑术"),
    Broadsword(4, "刀术"),
    Spear(5, "枪术"),
    Staff(6, "棍术"),
    NanDao(7, "南刀"),
    NanGun(8, "南棍"),
    TaiChiSword(9, "太极剑");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;

    RoutinesOptionalEnum(Integer key, String display) {
        this.key = key;
        this.display = display;
    }

    public Integer getKey() {
        return key;
    }

    public String getDisplay() {
        return display;
    }
}
