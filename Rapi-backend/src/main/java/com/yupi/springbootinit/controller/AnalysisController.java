package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcloud.cos.utils.CollectionUtils;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.UserInterfaceInfo;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.model.vo.InterfaceInvokeVO;
import com.yupi.springbootinit.model.vo.InterfaceinfoVO;
import com.yupi.springbootinit.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口分析控制器
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/intertface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInvokeVO>> getInterfaceInvokeTop3(){
        List<UserInterfaceInfo> interfaceInvokeTop3 = userInterfaceInfoMapper.getInterfaceInvokeTop3(5);
        Map<Long, List<UserInterfaceInfo>> interfaceinfoObjMap =
                interfaceInvokeTop3.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceinfoObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if(CollectionUtils.isNullOrEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInvokeVO> collect = list.stream().map(item -> {
            InterfaceInvokeVO interfaceInvokeVO = new InterfaceInvokeVO();
            BeanUtils.copyProperties(item, interfaceInvokeVO);
            int invokeNum = interfaceinfoObjMap.get(item.getId()).get(0).getTotalNum();
            interfaceInvokeVO.setInvokeNum(invokeNum);
            return interfaceInvokeVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(collect);

    }

}
