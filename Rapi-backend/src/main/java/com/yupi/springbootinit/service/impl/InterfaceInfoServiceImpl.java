package com.yupi.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.User;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.mapper.InterfaceInfoMapper;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoQueryRequest;
import com.yupi.springbootinit.model.entity.*;
import com.yupi.springbootinit.model.vo.InterfaceinfoVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Rui
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-07-04 16:47:19
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private UserService userService;
    
    @Override
    public void validInterfaceinfo(InterfaceInfo interfaceinfo, boolean add) {
        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceinfo.getName();
        String description = interfaceinfo.getDescription();
        String url = interfaceinfo.getUrl();
        String method = interfaceinfo.getMethod();
        String requestHeader = interfaceinfo.getRequestHeader();
        String responseHeader = interfaceinfo.getResponseHeader();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description,url,method,requestHeader,responseHeader), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }


    /**
     * 获取查询包装类
     *
     * @param interfaceinfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceinfoQueryRequest interfaceinfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceinfoQueryRequest == null) {
            return queryWrapper;
        }

        String sortField = interfaceinfoQueryRequest.getSortField();
        String sortOrder = interfaceinfoQueryRequest.getSortOrder();
        Long id = interfaceinfoQueryRequest.getId();
        String name = interfaceinfoQueryRequest.getName();
        String description = interfaceinfoQueryRequest.getDescription();
        Long userId = interfaceinfoQueryRequest.getUserId();
        String url = interfaceinfoQueryRequest.getUrl();
        Integer status = interfaceinfoQueryRequest.getStatus();

        // 添加条件
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like("description", description);
        }
        if (ObjectUtils.isNotEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        if (ObjectUtils.isNotEmpty(url)) {
            queryWrapper.eq("url", url);
        }
        if (ObjectUtils.isNotEmpty(id)) {
            queryWrapper.eq("id", id);
        }
        if (ObjectUtils.isNotEmpty(userId)) {
            queryWrapper.eq("userId", userId);
        }

        // 处理排序
        if (StringUtils.isNotBlank(sortField)) {
            boolean isAsc = sortOrder.equals(CommonConstant.SORT_ORDER_ASC);
            queryWrapper.orderBy(SqlUtils.validSortField(sortField), isAsc, sortField);
        }

        return queryWrapper;
    }


    @Override
    public InterfaceinfoVO getInterfaceinfoVO(InterfaceInfo interfaceinfo, HttpServletRequest request) {
        InterfaceinfoVO interfaceinfoVO = InterfaceinfoVO.objToVo(interfaceinfo);
//        long interfaceinfoId = interfaceinfo.getId();
        // 1. 关联查询用户信息
        Long userId = interfaceinfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceinfoVO.setUserVO(userVO);
        return interfaceinfoVO;
    }

    @Override
    public Page<InterfaceinfoVO> getInterfaceinfoVOPage(Page<InterfaceInfo> interfaceinfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceinfoList = interfaceinfoPage.getRecords();
        Page<InterfaceinfoVO> interfaceinfoVOPage = new Page<>(interfaceinfoPage.getCurrent(), interfaceinfoPage.getSize(), interfaceinfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceinfoList)) {
            return interfaceinfoVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = interfaceinfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        
        
        // 填充信息
        List<InterfaceinfoVO> interfaceinfoVOList = interfaceinfoList.stream().map(interfaceinfo -> {
            InterfaceinfoVO interfaceinfoVO = InterfaceinfoVO.objToVo(interfaceinfo);
            Long userId = interfaceinfo.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            interfaceinfoVO.setUserVO(userService.getUserVO(user));
            return interfaceinfoVO;
        }).collect(Collectors.toList());
        interfaceinfoVOPage.setRecords(interfaceinfoVOList);
        return interfaceinfoVOPage;
    }
    
}




