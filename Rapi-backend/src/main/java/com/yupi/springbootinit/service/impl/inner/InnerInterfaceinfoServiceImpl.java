package com.yupi.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.User;
import com.yupi.rapicommon.service.InnerInterfaceInfoService;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.InterfaceInfoMapper;
import com.yupi.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceinfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper  interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceinfo(String path, String method) {
        if(StringUtils.isAnyBlank(method,path)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("method",method);
        queryWrapper.eq("url",path);
        InterfaceInfo interfaceInfo =  interfaceInfoMapper.selectOne(queryWrapper);

        return interfaceInfo;
    }
}
