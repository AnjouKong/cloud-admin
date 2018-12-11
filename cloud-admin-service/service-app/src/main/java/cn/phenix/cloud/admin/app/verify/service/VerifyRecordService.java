package cn.phenix.cloud.admin.app.verify.service;//package cn.phenix.cloud.admin.tenant.verify.service;

import cn.phenix.cloud.admin.app.verify.dao.VerifyRecordMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.tenant.verify.VerifyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 马刚铭
 */
@Service
@Transactional(readOnly = true)
public class VerifyRecordService extends CoreService<VerifyRecordMapper, VerifyRecord> {
    @Autowired
    private VerifyRecordMapper verifyRecordMapper;

}
