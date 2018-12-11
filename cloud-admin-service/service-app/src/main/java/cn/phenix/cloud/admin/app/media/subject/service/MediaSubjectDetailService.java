package cn.phenix.cloud.admin.app.media.subject.service;

import cn.phenix.cloud.admin.app.media.subject.dao.MediaSubjectDetailMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaSubjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MediaSubjectDetailService extends CoreService<MediaSubjectDetailMapper, MediaSubjectDetail> {

    public PageTable<MediaSubjectDetail> findPage(DataTableParameter parameter, MediaSubjectDetail subjectDetail) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaSubjectDetail");
        finder.whereExcludeDel();
        finder.search("subjectId", subjectDetail.getSubjectId());
        finder.append(" order by location");
        Page<MediaSubjectDetail> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
