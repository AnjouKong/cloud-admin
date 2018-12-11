package cn.phenix.cloud.admin.mechanism.utils

import cn.phenix.model.mechanism.AttMain
import cn.phenix.tools.file.upload.FileModel
import org.apache.commons.lang3.math.NumberUtils

fun AttMain.toFileModel(model: FileModel) {
    this.id = model.attId
    this.fdFileName = model.fileName
    this.fdContentType = model.contentType
    //setCreateDate(new Date());
    this.fdFilePath = model.filePath
    this.crc32 = model.crc32
    this.fdFileType = model.ext
    this.fdSize = NumberUtils.toDouble(model.fileSize.toString(), 0.00)
    this.fdStoreType = model.storeType.getName()
    this.token = model.token
}