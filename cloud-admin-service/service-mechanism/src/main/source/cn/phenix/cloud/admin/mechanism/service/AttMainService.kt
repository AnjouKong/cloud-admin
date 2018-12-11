package cn.phenix.cloud.admin.mechanism.service

import cn.phenix.cloud.admin.mechanism.dao.AttMainMapper
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm
import cn.phenix.cloud.core.utils.IdGen
import cn.phenix.cloud.core.utils.MyBeanUtils
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.dynamic.Parameter
import cn.phenix.common.utils.Collections3
import cn.phenix.model.mechanism.AttMain
import jodd.io.FileNameUtil
import jodd.io.FileUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.IOException

@Service
@Transactional(readOnly = true)
class AttMainService : CoreService<AttMainMapper, AttMain>() {

    /**
     * 更新文档信息
     *
     * @param attMainForm
     * @param modelId
     * @param modelName
     */
    @Transactional
    fun updateAttMainForm(attMainForm: AttMainForm, modelId: String, modelName: String) {
        val map = MyBeanUtils.describe(attMainForm)
        map.keys
                .filter { "class" != it }
                .map { map[it] as List<AttMain> }
                .filterNot { Collections3.isEmpty(it) }
                .flatMap { it }
                .filter { StringUtils.isNotBlank(it.id) }
                .forEach { updateAttMain(it.id, modelId, modelName, it.fdKey) }
    }

    /**
     *
     */
    @Transactional(readOnly = false)
    fun saveAttMain(attMain: AttMain): AttMain {
        val filePath = attMain.fdFilePath
        if (StringUtils.isNotBlank(filePath)) {
            val prefix = filePath.substring(filePath.lastIndexOf(".") + 1)
            //文件后缀名
            attMain.fdFileType = prefix
        }
        //修改token值
        val id = attMain.id
        if (StringUtils.isNotBlank(id)) {
            attMain.token = attMain.id
        } else {
            attMain.token = IdGen.uuid()
        }
        return save(attMain)
    }

    @Transactional(readOnly = false)
    fun updateAttMain(id: String, modelId: String, modelName: String): AttMain {
        val parameter = Parameter.create().insert("fdModelId", modelId).insert("fdModelName", modelName)
        val where = Parameter.create().insert("id", id)
        updateByUpdate(parameter, where)
        return get(id)
    }

    @Transactional
    fun updateAttMain(id: String, modelId: String, modelName: String, fdKey: String): AttMain {
        val parameter = Parameter.create().insert("fdModelId", modelId).insert("fdModelName", modelName).insert("fdKey", fdKey)
        val where = Parameter.create().insert("id", id)
        updateByUpdate(parameter, where)
        return get(id)
    }

    @Transactional(readOnly = false)
    fun updateAttMains(attMains: List<AttMain>, modelId: String, modelName: String): List<AttMain> {
        for (attMain in attMains) {
            updateAttMain(attMain.id, modelId, modelName)
        }
        return attMains
    }


    fun getAttMain(modelId: String, modelName: String): List<AttMain> {
        val finder = Finder.create("from AttMain where fdModelId=:modelId").setParam("modelId", modelId)
                .append("and fdModelName=:modelName").setParam("modelName", modelName)
                .append(" order by fdFileName")
        return dao.find(finder)
    }

    /**
     * 根据modelId查找附件
     */
    fun getAttMainByModelId(modelId: String): List<AttMain> {
        val finder = Finder.create("from AttMain where fdModelId=:modelId").setParam("modelId", modelId)
        return dao.find(finder)
    }

    fun getAttMain(modelId: String, modelName: String, fdKey: String): List<AttMain> {
        val finder = Finder.create("from AttMain where fdModelId=:modelId").setParam("modelId", modelId)
                .append("and fdModelName=:modelName").setParam("modelName", modelName)
                .append("and fdKey=:fdKey").setParam("fdKey", fdKey)
        return dao.find(finder)
    }

    /**
     * 根据modelId和modelName删除附件信息
     *
     * @param modelId
     * @param modelName
     * @return
     */
    @Transactional(readOnly = false)
    fun deleteAttMain(modelId: String, modelName: String) {
        val attMains = getAttMain(modelId, modelName)
        if (attMains.isNotEmpty()) {
            for (attMain in attMains) {
                dao.delete(attMain)
            }
        }
    }


    @Transactional(readOnly = false)
    fun deleteAttMainById(id: String) {
        dao.deleteById(id)
    }


    fun getAttMainUniqueByToken(token: String): AttMain? {
        val finder = Finder.create("from AttMain where token=:token").setParam("token", token)
        val attMains = dao.find(finder)
        return if (attMains.isEmpty()) null else attMains.get(0)
    }

    /**
     * 根据token获取文档，如果存在新建一个文档，并把id值为新的，modelId和modelName值为null
     *
     * @param token
     * @return
     */
    fun getAttMainbyToken(token: String): AttMain? {
        return getAttMainUniqueByToken(token) ?: return null
    }

    /**
     * 根据token获取文档，如果存在新建一个文档，并把id值为新的，modelId和modelName值为null
     *
     * @param token
     * @return
     */
    @Transactional(readOnly = false)
    fun convertAttMainbyToken(token: String): AttMain? {
        val attMain = getAttMainUniqueByToken(token) ?: return null
        val convertAttMain = AttMain()
        MyBeanUtils.copyProperties(attMain, convertAttMain)
        convertAttMain.id = IdGen.uuid()
        convertAttMain.fdModelId = null
        convertAttMain.fdModelName = null
        saveAttMain(convertAttMain)
        return convertAttMain
    }

    @Transactional(readOnly = false)
    fun update(attMain: AttMain): AttMain = saveOrUpdate(attMain)


    @Transactional(readOnly = false)
    fun flush() {
        dao.flush()
    }

    private fun deleteFile(filePath: String, attId: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()

            var swfPath = FileNameUtil.getFullPath(filePath)
            swfPath += attId
            val swfFile = File(swfPath)
            if (swfFile.exists() && swfFile.isDirectory) {
                try {
                    FileUtil.deleteDir(swfFile)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

            }
        }
    }

}