package cn.phenix.cloud.admin.app.media.subject.service;

import cn.phenix.cloud.admin.app.media.subject.dao.MediaSubjectMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class MediaSubjectService extends CoreService<MediaSubjectMapper, MediaSubject> {

    public PageTable<MediaSubject> findPage(DataTableParameter parameter, MediaSubject subject) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaSubject");
        finder.whereExcludeDel();
        finder.search("subjectName", subject.getSubjectName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc");
        Page<MediaSubject> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    public List<MediaSubject> findByDelFlag() {
        return dao.find(Finder.create("from MediaSubject").whereExcludeDel());
    }

}
