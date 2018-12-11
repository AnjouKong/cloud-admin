package cn.phenix.cloud.admin.app.newMedia.subject.service;

import cn.phenix.cloud.admin.app.newMedia.subject.dao.NewMediaSubjectMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class NewMediaSubjectService extends CoreService<NewMediaSubjectMapper, NewMediaSubject> {

    public PageTable<NewMediaSubject> findPage(DataTableParameter parameter, NewMediaSubject subject) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaSubject");
        finder.whereExcludeDel();
        finder.search("subjectName", subject.getSubjectName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc");
        Page<NewMediaSubject> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    public List<NewMediaSubject> findByDelFlag() {
        return dao.find(Finder.create("from NewMediaSubject").whereExcludeDel());
    }

}
