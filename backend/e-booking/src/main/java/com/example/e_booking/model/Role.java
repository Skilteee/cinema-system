package com.example.e_booking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) { // 忽略大小写匹配
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role: " + value);
    }

    @JsonValue
    public String toJson() {
        return name(); // 返回大写的枚举值
    }
}