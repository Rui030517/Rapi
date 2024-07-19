package com.yupi.springbootinit.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.rapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author Rui
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-07-09 17:02:59
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> getInterfaceInvokeTop3(int limit);

}




