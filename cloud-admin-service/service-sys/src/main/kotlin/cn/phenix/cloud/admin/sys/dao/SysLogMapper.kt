package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysLog

/**
 * 系统日志
 *
 * @author mgm
 * @create 2018-2-11 15:54:56
 */
interface SysLogMapper : GenericJpaRepository<SysLog, String>{
}
