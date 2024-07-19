package com.yupi.springbootinit.controller;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.rapiclientsdk.client.RapiClient;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.User;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.*;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoAddRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoInvokeRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoQueryRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoUpdateRequest;
import com.yupi.springbootinit.model.enums.InterfaceinfoStatusEnums;
import com.yupi.springbootinit.model.vo.InterfaceinfoVO;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口管理
 *
 */
@RestController
@RequestMapping("/interfaceinfo")
@Slf4j
public class InterfaceinfoController {

    @Resource
    private InterfaceInfoService interfaceinfoService;

    @Resource
    private UserService userService;

    @Resource
    private RapiClient rapiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceinfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceinfo(@RequestBody InterfaceinfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceinfo);
        interfaceinfo.setCreateTime(DateTime.now());
        interfaceinfo.setUpdateTime(DateTime.now());

        // 参数校验
        interfaceinfoService.validInterfaceinfo(interfaceinfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());

        boolean result = interfaceinfoService.save(interfaceinfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceinfoId = interfaceinfo.getId();
        return ResultUtils.success(newInterfaceinfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceinfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceinfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceinfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceinfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceinfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceinfo(@RequestBody InterfaceinfoUpdateRequest interfaceinfoUpdateRequest) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, interfaceinfo);
        interfaceinfo.setUpdateTime(DateTime.now());

        // 参数校验
        interfaceinfoService.validInterfaceinfo(interfaceinfo, false);
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceinfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceinfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceinfoService.updateById(interfaceinfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceinfoVO> getInterfaceinfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = interfaceinfoService.getById(id);
        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceinfoService.getInterfaceinfoVO(interfaceinfo, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceinfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceinfoByPage(@RequestBody InterfaceinfoQueryRequest interfaceinfoQueryRequest) {
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        if(size >50 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceinfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceinfoVO>> listInterfaceinfoVOByPage(@RequestBody InterfaceinfoQueryRequest interfaceinfoQueryRequest,
            HttpServletRequest request) {
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoService.getInterfaceinfoVOPage(interfaceinfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceinfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceinfoVO>> listMyInterfaceinfoVOByPage(@RequestBody InterfaceinfoQueryRequest interfaceinfoQueryRequest,
            HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceinfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoService.getInterfaceinfoVOPage(interfaceinfoPage, request));
    }


    /**
     * 上线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceinfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceinfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceinfo == null, ErrorCode.NOT_FOUND_ERROR);

        //先调试判断一下该接口是否可以调用，可以正常调用的才能上线
        com.yupi.rapiclientsdk.modal.User user = new com.yupi.rapiclientsdk.modal.User();
        user.setUsername("ruiTest");
        String re = rapiClient.getUserNameByPost(user);

        if(!StringUtils.isNotBlank(re)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口调用失败");
        }

        //更新接口状态
        oldInterfaceinfo.setStatus(InterfaceinfoStatusEnums.ONLINE.getValue());
        boolean result = interfaceinfoService.updateById(oldInterfaceinfo);

        return ResultUtils.success(result);
    }


    /**
     * 下线接口（仅管理员）
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceinfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceinfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceinfo == null, ErrorCode.NOT_FOUND_ERROR);

        //更新接口状态
        oldInterfaceinfo.setStatus(InterfaceinfoStatusEnums.OFFLINE.getValue());
        boolean result = interfaceinfoService.updateById(oldInterfaceinfo);

        return ResultUtils.success(result);
    }


    /**
     * 调用接口
     *
     * @param interfaceinfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Object> invokeInterfaceinfo(@RequestBody InterfaceinfoInvokeRequest interfaceinfoInvokeRequest,HttpServletRequest httpServletRequest) {
        if (interfaceinfoInvokeRequest == null || interfaceinfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Long id = interfaceinfoInvokeRequest.getId();
        InterfaceInfo oldInterfaceinfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceinfo == null, ErrorCode.NOT_FOUND_ERROR);
        if(oldInterfaceinfo.getStatus() == InterfaceinfoStatusEnums.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }

        String userRequestParams = interfaceinfoInvokeRequest.getUserRequestParams();
        User loginUser = userService.getLoginUser(httpServletRequest);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        RapiClient templeClient = new RapiClient(accessKey,secretKey);
        Gson gson = new Gson();
        com.yupi.rapiclientsdk.modal.User user = gson.fromJson(userRequestParams, com.yupi.rapiclientsdk.modal.User.class);
        String userNameByPost = templeClient.getUserNameByPost(user);


        return ResultUtils.success(userNameByPost);
    }

}
