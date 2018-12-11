package cn.phenix.cloud.admin.app.newMedia.subject.service;

import cn.phenix.cloud.admin.app.newMedia.subject.dao.NewMediaSubjectDetailMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaSubjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class NewMediaSubjectDetailService extends CoreService<NewMediaSubjectDetailMapper, NewMediaSubjectDetail> {

    public PageTable<NewMediaSubjectDetail> findPage(DataTableParameter parameter, NewMediaSubjectDetail subjectDetail) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaSubjectDetail");
        finder.whereExcludeDel();
        finder.search("subjectId", subjectDetail.getSubjectId());
        finder.append(" order by location");
        Page<NewMediaSubjectDetail> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
