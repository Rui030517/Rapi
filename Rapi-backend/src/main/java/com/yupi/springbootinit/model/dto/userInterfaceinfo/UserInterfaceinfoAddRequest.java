package com.yupi.springbootinit.model.dto.userInterfaceinfo;

import lombok.Data;
import java.io.Serializable;


/**
 * 创建请求
 *
 */
@Data
public class UserInterfaceinfoAddRequest implements Serializable {


    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 被调用接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}