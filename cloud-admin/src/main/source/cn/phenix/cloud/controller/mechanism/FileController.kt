package cn.phenix.cloud.controller.mechanism

import cn.phenix.cloud.admin.mechanism.service.AttMainService
import cn.phenix.cloud.admin.mechanism.utils.toFileModel
import cn.phenix.cloud.controller.mechanism.StreamController.Companion.START_FIELD
import cn.phenix.cloud.controller.mechanism.TokenController.Companion.SUCCESS
import cn.phenix.cloud.controller.mechanism.TokenController.Companion.TOKEN_FIELD
import cn.phenix.cloud.controller.mechanism.Ueditor.map
import cn.phenix.cloud.utils.IOUtils
import cn.phenix.model.mechanism.AttMain
import cn.phenix.tools.file.upload.CommonFileUploadRepository
import cn.phenix.tools.file.upload.DefaultFileUploadRepository
import cn.phenix.tools.file.upload.FileModel
import cn.phenix.tools.file.upload.StoreType
import jodd.io.FileNameUtil
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/mechanism/file")
class FileController {

    private val logger = LoggerFactory.getLogger(FileController::class.java)

    @Autowired
    private val attMainService: AttMainService? = null

    @Autowired
    private val defaultFileUploadRepository: DefaultFileUploadRepository? = null

    @Autowired
    private val commonFileUploadRepository: CommonFileUploadRepository? = null

    @RequestMapping("/index")
    fun index(request: HttpServletRequest): String {
        return "file/index"
    }

    @RequestMapping("/view/{id}")
    fun view(request: HttpServletRequest, @PathVariable("id") id: String): String {
        var attMain = attMainService!!.get(id)
        request.setAttribute("url", attMain.fdFilePath)
        return "file/view"
    }


    @RequestMapping("/imageView/{modelId}/{modelName}")
    fun image(request: HttpServletRequest, response: HttpServletResponse, @PathVariable("modelId") modelId: String,
              @PathVariable("modelName") modelName: String) {
        response.setHeader("Cache-Control", "no-cache")
        response.setHeader("Cache-Control", "no-store")
        val attMainList: List<AttMain> = attMainService!!.getAttMain(modelId, modelName)

        if (!CollectionUtils.isEmpty(attMainList)) {
            out(response, attMainList[0].fdFilePath, attMainList[0].fdStoreType)
        }
    }

    private fun out(response: HttpServletResponse, fdFilePath: String, storeType: String) {
        response.setHeader("Cache-Control", "no-cache")
        response.setHeader("Cache-Control", "no-store")
        val inputStream: InputStream
        try {
            inputStream = if (StoreType.OSS.getName() == storeType) {
                URL(fdFilePath).openStream()
            } else {
                val file = File(fdFilePath)
                if (!file.exists()) {
                    return
                }
                FileInputStream(fdFilePath)
            }
            IOUtils.out(response.outputStream, inputStream)
            IOUtils.close(inputStream)
        } catch (e: IOException) {
            logger.error("查看图片附件异常", e)
        }
    }


    @RequestMapping("/delete/{id}")
    @ResponseBody
    fun delete(request: HttpServletRequest, @PathVariable("id") id: String): Map<String, Any> {
        val json = HashMap<String, Any>()
        attMainService!!.deleteAttMainById(id)
        json.put("status", "1")
        return json
    }

    /**
     * 上传文件
     *
     * @throws IOException
     */
    @RequestMapping("/o_upload")
    @ResponseBody
    @Throws(IOException::class)
    fun upload(request: HttpServletRequest): Map<String, Any> {
        val json = HashMap<String, Any>()
        val multipartRequest = request as MultipartHttpServletRequest
        val file = multipartRequest.getFile("Filedata")
        var token = request.getParameter(TOKEN_FIELD)
        if (StringUtils.isBlank(token)) {
            token = generateToken(file!!.originalFilename, file.size.toString())
        }
        var attMain = attMainService!!.convertAttMainbyToken(token)
        if (attMain != null) {
            json.put(START_FIELD, attMain.fdSize)
            json.put("fileName", attMain.fdFileName)
            json.put("attId", attMain.id)
            json.put("fileType", FileNameUtil.getExtension(attMain.fdFileName))
            json.put(SUCCESS, true)
            defaultFileUploadRepository!!.deleteToken(token)
            return json
        }
        var success = true
        try {
            val commonsMultipartResolver = CommonsMultipartResolver(
                    request.getSession().servletContext)
            commonsMultipartResolver.setDefaultEncoding("utf-8")

            val multipartFileMap = multipartRequest.fileMap
            for (key in multipartFileMap.keys) {
                val multipartFile = multipartFileMap[key]

                val fileModel = defaultFileUploadRepository!!.storeByExt(multipartFile, token)
                fileModel.token = token
                attMain = AttMain()
                attMain.toFileModel(fileModel)
                attMain = attMainService!!.saveAttMain(attMain)
                fileModel.attId = attMain.id
                json.put(START_FIELD, fileModel.fileSize)
                json.put("fileName", fileModel.fileName)
                json.put("attId", fileModel.attId)
                json.put("fileType", FileNameUtil.getExtension(attMain.fdFileName))
            }

        } catch (ex: Exception) {
            success = false
            json.put("status", 0)
            // ex.printStackTrace();
            //message = "Error: " + ex.getLocalizedMessage();
        }

        json.put("status", 1)
        json.put(SUCCESS, success)
        return json
    }

    /**
     * 上传文件
     */
    @GetMapping("/ueditor_upload")
    @ResponseBody
    fun ueditorUploadGet(request: HttpServletRequest): Map<String, Any>? {
        return Ueditor.map
    }

    /**
     * 上传文件
     */
    @PostMapping("/ueditor_upload")
    @ResponseBody
    fun ueditorUpload(request: HttpServletRequest): Map<String, Any>? {
        val isMultipart = ServletFileUpload.isMultipartContent(request)
        if (!isMultipart) {
            return Ueditor.map
        }
        val multipartRequest = request as MultipartHttpServletRequest
        val multipartFile = multipartRequest.getFile("upfile")
        var token = request.getParameter(TokenController.TOKEN_FIELD)
        if (StringUtils.isBlank(token)) {
            token = generateToken(multipartFile!!.originalFilename, multipartFile.size.toString())
        }

        var attMain = attMainService!!.convertAttMainbyToken(token)
        if (attMain != null) {
            defaultFileUploadRepository!!.deleteToken(token)
            return null
        }
        val fileModels = ArrayList<FileModel>()
        var success = true
        try {
            val commonsMultipartResolver = CommonsMultipartResolver(
                    request.getSession().servletContext)
            commonsMultipartResolver.setDefaultEncoding("utf-8")

            val fileModel = defaultFileUploadRepository!!.storeByExt(multipartFile, token)
            fileModel.token = token
            attMain = AttMain()
            attMain.toFileModel(fileModel)
            attMain.fdModelId = "Ueditor"
            attMain.fdModelName = "Ueditor"
            attMain = attMainService!!.saveAttMain(attMain)
            fileModel.attId = attMain.id
            fileModels.add(fileModel)

            map.put("url", attMain.fdFilePath)
            map.put("fileType", attMain.fdFileType)
            map.put("state", "SUCCESS")
            map.put("original", attMain.fdFileName)

        } catch (ex: Exception) {
            success = false
        }
        return map
    }


    @Throws(IOException::class)
    private fun generateToken(name: String?, size: String?): String {
        if (name == null || size == null)
            return ""
        val code = name.hashCode()
        try {
            val token = ((if (code > 0) "A" else "B") + Math.abs(code) + "_"
                    + size.trim { it <= ' ' })
            /** TODO: store your token, here just create a file  */
            defaultFileUploadRepository!!.storeToken(token)

            return token
        } catch (e: Exception) {
            throw IOException(e)
        }

    }
}