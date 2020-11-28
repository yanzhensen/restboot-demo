package com.sam.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sam.common.exception.UnknownEnumException;
import com.sam.framework.enums.IEnum;

/**
 * <p>
 * 状态枚举
 * </p>
 *
 * @author Caratacus
 */
public enum StatusEnum implements IEnum {

    NORMAL(1), DISABLE(0);

    @EnumValue
    private final int value;

    StatusEnum(final int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return this.value;
    }

    //序列化使用
    @JsonCreator
    public static StatusEnum getEnum(int value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.getValue() == value) {
                return statusEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid StatusEnum type value: " + value);
    }
}
