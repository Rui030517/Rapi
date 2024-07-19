package com.yupi.springbootinit.model.vo;

import com.yupi.rapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInvokeVO extends InterfaceInfo {

    /**
     * 调用次数
     */
    private Integer invokeNum;

    private static final long serialVersionUID = 1L;

}
