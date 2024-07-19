package com.yupi.springbootinit.service.impl.inner;

import com.yupi.rapicommon.model.entity.UserInterfaceInfo;
import com.yupi.rapicommon.service.InnerUserInterfaceinfoService;
import com.yupi.springbootinit.service.UserInterfaceinfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceinfoServiceImpl implements InnerUserInterfaceinfoService {

    @Resource
    private UserInterfaceinfoService userInterfaceinfoService;

    @Override
    public boolean invokeCount(long interfaceinfoId, long userId) {
        return userInterfaceinfoService.invokeCount(interfaceinfoId, userId);
    }

}
