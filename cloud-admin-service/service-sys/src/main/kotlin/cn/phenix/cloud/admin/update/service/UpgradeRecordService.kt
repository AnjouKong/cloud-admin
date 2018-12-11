package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.UpgradeRecordMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.upgrade.UpgradeRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat



/**
 * 2018年1月3日
 */
@Service
@Transactional(readOnly = true)
class UpgradeRecordService : CoreService<UpgradeRecordMapper, UpgradeRecord>() {
    @Autowired
    private val upgradeRecordMapper: UpgradeRecordMapper? = null

    fun findPage(parameter: DataTableParameter, upgradeRecord: UpgradeRecord, sstartDate: String, estartDate: String): PageTable<UpgradeRecord> {
        val pageRequest = parameter.pageRequest

        val finder = Finder.create("select m from UpgradeRecord m where 1=1 ")
        finder.search("launcherName", upgradeRecord.launcherName, Finder.SearchType.LIKE)
        finder.search("launcherVersionName", upgradeRecord.launcherVersionName, Finder.SearchType.LIKE)
        finder.search("launcherVersion", upgradeRecord.launcherVersion, Finder.SearchType.LIKE)

        if(sstartDate != ""){
            val sdf = SimpleDateFormat("yyyy-MM-dd")//小写的mm表示的是分钟
            val date = sdf.parse(sstartDate)
            finder.search("createDate", date, Finder.SearchType.GE)
        }
        if(estartDate != "") {
            val sdf = SimpleDateFormat("yyyy-MM-dd")//小写的mm表示的是分钟
            val date = sdf.parse(estartDate)
            finder.search("createDate", date, Finder.SearchType.LE)
        }
        finder.append("order by m.createDate desc")
        val page = upgradeRecordMapper!!.find(finder, pageRequest)
        return PageTable(page)
    }
}
