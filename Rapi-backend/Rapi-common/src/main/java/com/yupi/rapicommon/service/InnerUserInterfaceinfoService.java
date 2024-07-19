package com.yupi.rapicommon.service;



public interface InnerUserInterfaceinfoService {


    /**
     * 调用用户接口
     * @param interfaceinfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceinfoId,long userId);


}
