package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceinfoQueryRequest;
import com.yupi.springbootinit.model.vo.InterfaceinfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Rui
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-04 16:47:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceinfo(InterfaceInfo interfaceinfo, boolean b);

    InterfaceinfoVO getInterfaceinfoVO(InterfaceInfo interfaceinfo, HttpServletRequest request);

    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceinfoQueryRequest interfaceinfoQueryRequest);

    Page<InterfaceinfoVO> getInterfaceinfoVOPage(Page<InterfaceInfo> interfaceinfoPage, HttpServletRequest request);
}
