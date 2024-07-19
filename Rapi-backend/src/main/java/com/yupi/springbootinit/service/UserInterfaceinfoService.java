package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.rapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceinfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceinfo(UserInterfaceInfo userInterfaceInfo, boolean b);


    boolean invokeCount(long interfaceinfoId, long userId);
}
