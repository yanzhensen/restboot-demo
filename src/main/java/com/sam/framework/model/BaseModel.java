package com.sam.framework.model;

import com.sam.framework.model.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自增主键父类
 * </p>
 *
 * @author Caratacus
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseModel extends Convert {

    protected Integer mid;

}
