package com.sam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sam.framework.enums.IEnum;

/**
 * <p>
 * 资历枚举
 * </p>
 *
 * @author wangchong
 */
public enum SeniorityEnum implements IEnum {

    /**
     * 大佬
     */
    MOGUL(0),

    /**
     * 新手
     */
    NOVICE(1);

    @EnumValue
    private final int value;

    SeniorityEnum(final int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return this.value;
    }
}
