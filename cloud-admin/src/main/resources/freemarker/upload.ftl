<#setting classic_compatible=true>
<script type="text/javascript">
    function deleteAttMain${id}(attId) {
        $.ajax({
            type: "POST",
            cache: false,
            url: "/mechanism/file/delete/" + attId,
            async: false,
            success: function (result) {
                if (result.status == '1') {
                    $("#div_" + attId).remove();
                    $("#tr_" + attId).remove();
                    indexx${id}--;
                    var idvalue = $("#hidden_${id}").attr("value");
                    if (idvalue == attId) {
                        $("#hidden_${id}").attr({"value": "", "name": ""});
                    }
                }
            }, error: function () {
            }
        });
    }
    <#if method?? && method!='view'>
    var indexx${id } = ${attMains?size};
    var config_${id} = {
        customered: true,
        multipleFiles: true,
        fileFieldName: "FileData",
        maxSize: 2147483648,
        /** 当_t.bStreaming = false 时（也就是Flash上传时），2G就是最大的文件上传大小！所以一般需要 */
        simLimit: 10000,
        /** 允许同时选择文件上传的个数（包含已经上传过的） */
        extFilters: [${ext}], /** 默认是全部允许，即 [] */
        browseFileId: "i_select_files_${id}",
        /** 文件选择的Dom Id，如果不指定，默认是i_select_files */
        browseFileBtn: "<div>请选择文件</div>",
        /** 选择文件的按钮内容，非自定义UI有效(customered:false) */
        swfURL: "/static/stream/swf/FlashUploader.swf", /** SWF文件的位置 */
        tokenURL: "/mechanism/token/file?imgCheck=${imgCheck!}&resizeImg=${resizeImg!}",
        /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
        frmUploadURL: "/mechanism/file/o_upload?imgCheck=${imgCheck!}&resizeImg=${resizeImg!}&resizeWidth=${resizeWidth!}&resizeHeight=${resizeHeight!}&exts=${exts!}",
        /** Flash上传的URI */
        uploadURL: "/mechanism/html5/upload?imgCheck=${imgCheck!}&resizeImg=${resizeImg!}&resizeWidth=${resizeWidth!}&resizeHeight=${resizeHeight!}&exts=${exts!}",
        /** HTML5上传的URI */
        onMaxSizeExceed: function (file) {
            $("#i_error_tips > span.text-message").append("文件[name=" + file.name + ", size=" + file.formatSize + "]超过文件大小限制‵" + file.formatLimitSize + "‵，将不会被上传！<br>");
        },
        onFileCountExceed: function (selected, limit) {
            $("#i_error_tips > span.text-message").append("同时最多上传<strong>" + limit + "</strong>个文件，但是已选择<strong>" + selected + "</strong>个<br>");
        },
        onExtNameMismatch: function (info) {
            console && console.log("-------------onExtNameMismatch-------------------");
            console && console.log(info);
            alert("文件类型不匹配");
            console && console.log("-------------onExtNameMismatch-------------------End");
        },
        onAddTask: function (file) {
            <#if multi=='true'>
                var fileHtml = '<tr id="' + file.id + '" class="template-upload fade in">' +
                        '<td><p class="name">' + file.name + '</p>' +
                    <#if showUploadInfo=='true'>
                            ' <div><span class="label label-info">进度：</span> <span class="message-text"></span></div>' +
                    </#if>
                        '</td>' +
                        '<td><a class="glyphicon glyphicon-remove" onClick="javascript:_t_${id}.cancelOne(\'' + file.id + '\')">移除</a>|' +
                        '<a style="cursor: pointer;" class="glyphicon glyphicon-see" target="_blank" href="">查看</a>' +
                        '</td></tr>';
                $("#bootstrap-stream-container_${id}").append(fileHtml);
            <#else>
                var fileHtml = '<tr id="' + file.id + '" class="template-upload fade in">' +
                        '<td><p class="name">' + file.name + '</p>' +
                    <#if  showUploadInfo=='true'>
                            ' <div><span class="label label-info">进度：</span> <span class="message-text"></span></div>' +
                    </#if>
                        '</td>' +
                        '<td><a class="glyphicon glyphicon-remove" onClick="javascript:_t_${id}.cancelOne(\'' + file.id + '\')">移除</a>|' +
                        '<a style="cursor: pointer;" class="glyphicon glyphicon-see" target="_blank" href="">查看</a>' +
                        '</td></tr>';

                $("#bootstrap-stream-container_${id}").html(fileHtml);
            </#if>
        },
        onUploadProgress: function (file) {
            var $bar = $("#" + file.id).find("div.progress-bar");
            $bar.css("width", file.percent + "%");
            var $message = $("#" + file.id).find("span.message-text");
            $message.text("已上传:" + file.formatLoaded + "/" + file.formatSize + " 速 度:" + file.formatSpeed);
            $message.text("正在下发到CDN服务器...");
            console && console.log("-------------onUploadProgress-------------------End");
        },
        //
        onComplete: function (file) {
            console && console.log(file);
            try {
                var value = JSON.parse(file.msg);
                $("#hidden_${id}").attr({"value": value.attId, "name": value.fileUrl});
                console && console.log("http back:" + value);
                var $message = $("#" + file.id).find("span.message-text");
                if (value && value.status == '1') {
                    $message.text("附件上传完毕");
                    <#if callBack??>
                        try {
                            if (${callBack!}) {
                                ${callBack!}(value.attId, value.fileName, '${fdKey}');
                            }
                        } catch (e) {
                            console && console.log(e)
                        }

                    </#if>
                    <#if multi=='true'>

                        jQuery("#inputHidden${id }").append('<div id="div_' + value.attId + '"><input type="text" name="${name!}['
                                + indexx${id } + '].fdFileName" value="' + value.fileName + '" /><input type="text" name="${name!}['
                                + indexx${id } + '].id" value="' + value.attId + '" /><input type="text" name="${name!}['
                                + indexx${id } + '].fdKey" value="${fdKey!}" /></div>');
                        indexx${id}++;
                        $("#" + file.id).find(".glyphicon-remove").attr("onClick", "deleteAttMain${id }('" + value.attId + "')");
                        $("#" + file.id).find(".glyphicon-see").attr("href", "/mechanism/file/view/" + value.attId + "");
                        <#if showImg=='true'>

                            $("#" + file.id).find(".name").html("<img ${imgStyle} src='" + value.fileUrl + "?time=" + Math.random() + "'/>");
                        </#if>
                        $("#" + file.id).attr("id", "tr_" + value.attId);
                    <#else>
                        jQuery("#inputHidden${id }").html('<div id="div_' + value.attId + '"><input type="text" name="${name!}['
                                + indexx${id } + '].fdFileName" value="' + value.fileName + '" /><input type="text" name="${name!}['
                                + indexx${id } + '].id" value="' + value.attId + '" /><input type="text" name="${name!}['
                                + indexx${id } + '].fdKey" value="${fdKey!}" /></div>');
                        indexx${id}++;
                        $("#" + file.id).find(".glyphicon-remove").attr("onClick", "deleteAttMain${id }('" + value.attId + "')");
                        $("#" + file.id).find(".glyphicon-see").attr("href", "/mechanism/file/view/" + value.attId + "");
                        <#if showImg=='true'>
                            $("#" + file.id).find(".name").html("<img ${imgStyle} src='" + value.fileUrl + "?time=" + Math.random() + "'/>" +
                                    "<input type='hidden' name='upload_Pic_FileName' value='" + value.fileUrl + "' />")
                        </#if>
                        $("#" + file.id).attr("id", "tr_" + value.attId);
                    </#if>
                } else {
                    if ('${imgCheck!}' != '') {
                        $message.text("上传失败");
                        alert('上传失败,产生原因：1:文件名必须为${imgCheck!},2:上传出错');
                    } else {
                        $message.text("上传失败");
                        alert('上传失败.');
                    }
                }
            } catch (e) {
                alert(e.message);
            }
            //console && console.log("-------------onComplete-------------------End");
        },
        onUploadError: function (status, msg) {
            //console && console.log("-------------onUploadError-------------------");
            //console && console.log(msg + ", 状态码:" + status);

            $("#i_error_tips > span.text-message").append(msg + ", 状态码:" + status);

            //console && console.log("-------------onUploadError-------------------End");
        },
        onCancel: function (file) {
            console && console.log(file)
            alert(file.id);
        }
    };
    var _t_${id} = new Stream(config_${id});

    /** Flash最大支持2G */
    if (!_t_${id}.bStreaming) {
        _t_${id}.config.maxSize = 2147483648;
    }
    </#if>
</script>