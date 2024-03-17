package com.example.signproject.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryEnum {
    Sanda(0, "武术散打"),
    Aerobics(1, "健美操"),
    Routine(2, "武术套路");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;

    CategoryEnum(Integer key, String display) {
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
