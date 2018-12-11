package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysGetWayMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.sys.SysGetWay
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 接口路由管理器
 */
@Service
@Transactional(readOnly = true)
class SysGetWayService : CoreService<SysGetWayMapper, SysGetWay>() {



}