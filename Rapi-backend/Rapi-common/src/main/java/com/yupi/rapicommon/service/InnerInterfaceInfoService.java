package com.yupi.rapicommon.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.vo.InterfaceinfoVO;


/**
* @author Rui
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-04 16:47:19
*/
public interface InnerInterfaceInfoService {

        /**
     * 从数据库中查询模拟接口是否存在
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceinfo(String path, String method);

}
