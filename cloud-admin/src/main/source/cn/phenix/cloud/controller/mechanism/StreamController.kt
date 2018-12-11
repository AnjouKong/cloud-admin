package cn.phenix.cloud.controller.mechanism

import cn.phenix.cloud.admin.mechanism.service.AttMainService
import cn.phenix.cloud.admin.mechanism.utils.toFileModel
import cn.phenix.model.mechanism.AttMain
import cn.phenix.tools.file.exception.StreamException
import cn.phenix.tools.file.upload.CommonFileUploadRepository
import cn.phenix.tools.file.upload.FileItem
import cn.phenix.tools.file.upload.FileModel
import jodd.io.FileNameUtil
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.io.*
import java.nio.charset.Charset
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/mechanism/html5/upload")
class StreamController {

    private val logger = LoggerFactory.getLogger(StreamController::class.java)

    @Autowired
    private val commonFileUploadRepository: CommonFileUploadRepository? = null

    companion object {
        internal val BUFFER_LENGTH = 10240
        val START_FIELD = "start"
    }

    @Autowired
    private val attMainService: AttMainService? = null

    /**
     * html5 上传文件
     */
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun execute(req: HttpServletRequest,
                resp: HttpServletResponse, model: ModelMap): Map<String, Any> {
        doOptions(req, resp)
        val token = req.getParameter(TokenController.TOKEN_FIELD)
        val size = req.getParameter(TokenController.FILE_SIZE_FIELD)
        val fileName = req.getParameter(TokenController.FILE_NAME_FIELD)

        val json = HashMap<String, Any>()
        val attMain = attMainService!!.convertAttMainbyToken(token)
        if (attMain != null) {
            json.put("fileName", attMain.fdFileName)
            json.put("attId", attMain.id)
            json.put("fileType", FileNameUtil.getExtension(attMain.fdFileName))
            json.put(START_FIELD, attMain.fdSize)
            json.put(TokenController.SUCCESS, true)
            json.put("status", 1)
            commonFileUploadRepository!!.deleteToken(token)
            return json
        }
        var start: Long = 0
        var success = true
        var message = ""
        try {
            val f = commonFileUploadRepository!!.getTokedFile(token)
            start = f.length()
            if (token.endsWith("_0") && "0" == size && 0L == start)
                f.renameTo(commonFileUploadRepository!!.getFile(token, fileName))
        } catch (e: IOException) {
            message = "Error: " + e.message
            success = false
        } finally {
            if (success)
                json.put(START_FIELD, start)
            json.put(TokenController.SUCCESS, success)
            json.put(TokenController.MESSAGE, message)
        }
        return json
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseBody
    @Throws(IOException::class)
    fun upload(req: HttpServletRequest,
               resp: HttpServletResponse, model: ModelMap): Map<String, Any> {
        doOptions(req, resp)

        val token = req.getParameter(TokenController.TOKEN_FIELD)
        var fileName = req.getParameter(TokenController.FILE_NAME_FIELD)
        /*val resizeImg = req.getParameter("resizeImg")
        val resizeWidth = req.getParameter("resizeWidth")
        val resizeHeight = req.getParameter("resizeHeight")
        val imgCheck = req.getParameter("imgCheck")*/
        fileName = String(fileName.toByteArray(charset("iso-8859-1")), Charset.forName("UTF-8"))
        val json = HashMap<String, Any>()


        val range = commonFileUploadRepository!!.parseRange(req)
        var fileModel = FileModel()
        var out: OutputStream? = null
        var content: InputStream? = null

        val contentType = req.contentType
        var start: Long = 0
        var success = true
        var message = ""
        val f = commonFileUploadRepository!!.getTokedFile(token)
        try {
            if (f.length() != range.from) {
                throw StreamException(StreamException.ERROR_FILE_RANGE_START)
            }
            out = FileOutputStream(f, true)
            content = req.inputStream

            var read = 0
            val bytes = ByteArray(BUFFER_LENGTH)
            while(true){
                read = content.read(bytes)
                if(read == -1) break
                out.write(bytes, 0, read)
            }
            start = f.length()
        } catch (se: StreamException) {
            success = StreamException.ERROR_FILE_RANGE_START == se.code
            message = "Code: " + se.code
            logger.error(message + ":" + se.message)
        } catch (fne: FileNotFoundException) {
            message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST
            success = false
            logger.error(fne.message)
        } catch (io: IOException) {
            message = "IO Error: " + io.message
            success = false
            logger.error(io.message)
        } finally {
            IOUtils.closeQuietly(out)
            IOUtils.closeQuietly(content)
            //val ext = FilenameUtils.getExtension(fileName)
            /** rename the file  */
            if (range.size == start) {
                /** fix the `renameTo` bug  */
                val dst = commonFileUploadRepository!!.getFile(token, fileName)
                dst!!.delete()
                f.renameTo(dst)

                val fileItem = FileItem(contentType,dst)
                try{
                    logger.info("开始下发到OSS服务----------------------")
                    attMainService!!.flush()
                    fileModel = commonFileUploadRepository!!.storeByExt(fileItem, token)
                    logger.info("OSS下发完毕----------------------")
                    var attMain = AttMain()
                    attMain.toFileModel(fileModel)
                    attMain.fdFileName = fileName
                    attMain = attMainService!!.saveAttMain(attMain)
                    fileModel.attId = attMain.id
                }catch (e : Exception){
                    logger.error("上传出现异常：",e)
                    json.put("status", 0)
                    json.put(TokenController.SUCCESS, false)
                    json.put("fileName", fileName)
                    json.put(TokenController.MESSAGE, e.message.toString())
                    return json
                }finally {
                    dst.delete()
                }
            }
        }
        if (success) {
            json.put(START_FIELD, start)
            json.put("fileName", fileModel.fileName)
            json.put("attId", fileModel.attId)
            json.put("fileUrl", fileModel.filePath)
            json.put("fileType", FileNameUtil.getExtension(fileModel.fileName))
        }
        json.put("status", 1)
        json.put(TokenController.SUCCESS, success)
        json.put("fileName", fileName)
        json.put(TokenController.MESSAGE, message)
        return json
    }

    private fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setHeader("Access-Control-Allow-Headers", "Content-Range,Content-Type")
        resp.setHeader("Access-Control-Allow-Origin", "*")
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
    }

}