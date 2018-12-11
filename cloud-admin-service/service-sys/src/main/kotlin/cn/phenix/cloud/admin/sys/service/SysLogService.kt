package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysLogMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.sys.SysLog
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 系统日志
 *
 * @author mgm
 * @create 2018-2-11 15:54:20
 */
@Service
@Transactional(readOnly = true)
class SysLogService : CoreService<SysLogMapper, SysLog>(){
}
