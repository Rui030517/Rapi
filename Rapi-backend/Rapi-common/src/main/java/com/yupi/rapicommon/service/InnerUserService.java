package com.yupi.rapicommon.service;


import com.yupi.rapicommon.model.entity.User;


/**
 * 用户服务
 *
 */
public interface InnerUserService{


        /**
     * 获取到数据库中分配给ak,sk的用户信息
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
