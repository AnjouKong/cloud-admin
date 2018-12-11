package cn.phenix.cloud.controller.mechanism

import cn.phenix.tools.file.upload.CommonFileUploadRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.io.IOException
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/mechanism/token")
class TokenController {
    companion object {
        internal val FILE_NAME_FIELD = "name"
        internal val FILE_SIZE_FIELD = "size"
        val TOKEN_FIELD = "token"
        val SUCCESS = "success"
        internal val MESSAGE = "message"
    }

    @RequestMapping(value = "/file", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    @Throws(IOException::class)
    fun execute(req: HttpServletRequest,
                res: HttpServletResponse): Map<String, Any> {
        val name = req.getParameter(FILE_NAME_FIELD)
        val size = req.getParameter(FILE_SIZE_FIELD)
        val token = generateToken(name, size)
        val json = HashMap<String, Any>()
        json.put(TOKEN_FIELD, token)
        json.put(SUCCESS, true)
        json.put(MESSAGE, "")
        return json
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
            commonFileUploadRepository!!.storeToken(token)

            return token
        } catch (e: Exception) {
            throw IOException(e)
        }

    }

    @Autowired
    private val commonFileUploadRepository: CommonFileUploadRepository? = null
}