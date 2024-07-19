package com.yupi.springbootinit.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.UserInterfaceInfo;
import com.yupi.rapicommon.service.InnerInterfaceInfoService;
import com.yupi.rapicommon.service.InnerUserInterfaceinfoService;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.UserInterfaceInfoMapper;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceinfoService;
import com.yupi.springbootinit.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Rui
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-07-09 17:02:59
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceinfoService {
   
    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @Override
    public void validUserInterfaceinfo(UserInterfaceInfo userInterfaceInfo, boolean b) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Long userId = userInterfaceInfo.getUserId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        // 创建时，参数不能为空
        if (interfaceInfoId <=0 || userId <=0 || totalNum <=0 || leftNum <=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (totalNum > 5000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用次数超限");
        }

    }


    @Override
    public boolean invokeCount(long interfaceinfoId, long userId) {
        if(interfaceinfoId <=0 || userId <=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //如果是本人的接口，那么不需要校验调用次数这些
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",interfaceinfoId);
        InterfaceInfo interfaceInfoOne = interfaceInfoService.getOne(queryWrapper);
        Long createUserId = interfaceInfoOne.getUserId();
        if(createUserId == userId) {
            //如果接口本来就是他自己创建的，统计调用次数就没必要了,直接可以调用
            return true;
        }

        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        userInterfaceInfoQueryWrapper.eq("interfaceinfoId",interfaceinfoId);
        userInterfaceInfoQueryWrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(userInterfaceInfoQueryWrapper);
        if(userInterfaceInfo == null) {  //如果没有调用过，那么给他初始调用次数1000次
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceInfoId(interfaceinfoId);
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setTotalNum(1);
            userInterfaceInfo.setLeftNum(999);
            return this.save(userInterfaceInfo);
        }

        //如果是调用过，有调用记录的话
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if(leftNum <=0 ){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"调用失败，请申请接口调用次数");
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceinfoId",interfaceinfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum = leftNum - 1 , totalNum = totalNum + 1");

        return this.update(updateWrapper);
    }



//    /**
//     * 获取查询包装类
//     *
//     * @param userInterfaceinfoQueryRequest
//     * @return
//     */
//    @Override
//    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceinfoQueryRequest userInterfaceinfoQueryRequest) {
//        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
//        if (userInterfaceinfoQueryRequest == null) {
//            return queryWrapper;
//        }
//
//        String sortField = userInterfaceinfoQueryRequest.getSortField();
//        String sortOrder = userInterfaceinfoQueryRequest.getSortOrder();
//        Long id = userInterfaceinfoQueryRequest.getId();
//        Long userId = userInterfaceinfoQueryRequest.getUserId();
//
//        Integer status = userInterfaceinfoQueryRequest.getStatus();
//
//        // 添加条件
//        if (ObjectUtils.isNotEmpty(status)) {
//            queryWrapper.eq("status", status);
//        }
//        if (ObjectUtils.isNotEmpty(id)) {
//            queryWrapper.eq("id", id);
//        }
//        if (ObjectUtils.isNotEmpty(userId)) {
//            queryWrapper.eq("userId", userId);
//        }
//
//        // 处理排序
//        if (StringUtils.isNotBlank(sortField)) {
//            boolean isAsc = sortOrder.equals(CommonConstant.SORT_ORDER_ASC);
//            queryWrapper.orderBy(SqlUtils.validSortField(sortField), isAsc, sortField);
//        }
//
//        return queryWrapper;
//    }



}





