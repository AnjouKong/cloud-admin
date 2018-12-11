
    var chks;
    // var gridster;
    // var picId;
    // var colPicId,focusPicId,clickPicId;
    var backgImage,focusImage,clickImage;
    var postType;
    var num;
    var modalStatic = true;
    var modalFocus = true;
    var modalClick = true;
    var category;

    //事件类型下拉列表
    function selectNum(){
        $(".selectNum").on("click","select",function(){
            $(".selectTab").show();
            $(".selectTab .table,.selectTab .custom,.selectTab .generalTVSignal,.selectTab .specialTVSignal").parent().hide();
            var dataTo = $(".selectNum").find("option:selected").attr("data-to");
            $(".selectTab").find("."+dataTo).parent().show();
            $(".selectNum .optionSel").remove();
            // var selNum = $(".selectNum").find("option:selected").val();
            if(dataTo == "custom"){
                $("#packageName,#action").val("");
                $(".custom").find("input[value='media']").prop("checked", true);
                $(".customParent").find(".dataTableMedia").parent().show();
                $(".custom").on("click","input",function(){
                    $(".customParent .table").parent().hide();
                     $("#div_mediaName").hide();
                    var dataCus = $(".custom").find("input[name='custom']:checked").attr("data-cus");
                    $(".customParent").find("."+dataCus).parent().show();
                    if(dataCus=='dataTableMedia'){
                      $("#div_mediaName").show();
                    }
                });
            }
        })
    }
    // ruleID
    function ruleId(){
        var ruleId = $(".selectNum").find("option:selected").val();
        switch (ruleId) {
            case "Container":// 本容器
                postType = "Container"; // ruleVal 跳转类型input的val
                chks = "null"; // rule 跳转类型id 或者文本
                break;
            case "image":// 图片
                postType = "image";
                chks = dataTableImg.rows('.selected').data();
                break;
            case "article":// 文章
                postType = "article";
                chks = dataTableArticle.rows('.selected').data();
                break;
            case "app":// app
                postType = "app";
                chks = dataTableApp.rows('.selected').data();
                break;
            case "imageList":// 图片列表
                postType = "imageList";
                chks = dataTableImgCollection.rows('.selected').data();
                break;
            case "video":// 视频
                postType = "video";
                chks = dataTableVideo.rows('.selected').data();
                break;
            case "adv":// 广告
                postType = "adv";
                chks = dataTableAdv.rows('.selected').data();
                break;
            case "GeneralTVSignal":// 普通电视信号
                postType = "GeneralTVSignal";
                var general = {
                    "packageName": $("input#packageNameGeneral").val(),
                    "action": $("input#actionGeneral").val()
                }
                chks = JSON.stringify(general);
                break;
            case "SpecialTVSignal":// 特殊电视信号
                postType = "SpecialTVSignal";
                var special = {
                    "packageName": $("input#packageNameSpecial").val(),
                    "action": $("input#actionSpecial").val(),
                    "source": $("input#sourceSpecial").val(),
                    "channel": $("input#channelSpecial").val()
                }
                chks = JSON.stringify(special);
                break;
            case "Custom":// 自定义
                postType = "Custom";
                var cusInput = $("input[name='custom']:checked").val();
                var customChks = {
                    "packageName": $("input#packageName").val(),
                    "action": $("input#action").val(),
                    "param": cusInput == "media" ? dataTableMedia.rows('.selected').data()[0].id : ""
                }
                chks = JSON.stringify(customChks);
                // console.log(chks);
                break;
        };
    }

    // 重置
    function modalReset(){
        $(".selectTab").hide();
        $("tbody tr").removeClass('selected');
        selectNum();
        var selCorner = $("#columnId").parent().attr("corner");
        var inpAuthority = $("#columnId").parent().attr("authority");
        // 名称等
        if($("#columnId").text() == "请编辑" || $("#columnId").text() == ""){
            backgImage = "";
            focusImage = "";
            clickImage = "";
            $("#editName,#staticBG,#staticColor,#staticSize,#staticFontBack,#focusBG,#focusColor,#focusSize,#focusFontBack,#clickBG,#clickColor,#clickSize,#clickFontBack").val("");
            $("#languageName").find("input").val("");
            $(".corner").find("option:first").prop("selected", true);//.trigger("click");
            $(".authority").find("input[value='1']").prop("checked", true);
            $("#staticBackgroundPicForm .imgUpload,#focusBackgroundPicForm .imgUpload,#clickBackgroundPicForm .imgUpload").remove();
            $(".selectNum .optionSel").remove();
            $(".selectNum option:first").prop("selected", true);
            $(".selectNum select").trigger("click");
        }else{
            backgImage = $("#columnId").parent().attr("backGroupImage");
            focusImage = $("#columnId").parent().attr("focusBGImg");
            clickImage = $("#columnId").parent().attr("clickBGImg");
            $("#languageName").find("input[type='text']").val("");
            // 多语言name
            var nameReset = JSON.parse($("#columnId").parent().attr("namelist"));
            var languageName = $("#languageName").find("input[type='hidden']");
            languageName.each(function(){
                for(var key in nameReset){
                    // console.log(key); //json对象的key
                    // console.log(nameReset[key]); //json对象的值
                    if($(this).attr("id") == key){
                        $(this).prev().val(nameReset[key]);
                    }
                }
            });

            $(".corner").find("option[value='"+selCorner+"']").prop("selected", true);
            $(".authority").find("input[value='"+inpAuthority+"']").prop("checked", true);
            $("#staticBG").val($("#columnId").parent().css("background-color"));
            $("#staticColor").val($("#columnId").parent().css("color"));
            $("#staticSize").val($("#columnId").parent().css("font-size"));
            $("#staticFontBack").val($("#columnId").parent().attr("staticFontBack"));
            $("#focusBG").val($("#columnId").parent().attr("focusBG"));
            $("#focusColor").val($("#columnId").parent().attr("focusColor"));
            $("#focusSize").val($("#columnId").parent().attr("focusSize"));
            $("#focusFontBack").val($("#columnId").parent().attr("focusFontBack"));
            $("#clickBG").val($("#columnId").parent().attr("clickBG"));
            $("#clickColor").val($("#columnId").parent().attr("clickColor"));
            $("#clickSize").val($("#columnId").parent().attr("clickSize"));
            $("#clickFontBack").val($("#columnId").parent().attr("clickFontBack"));
        };
        // 静态
        var imgBGUpload = $("#columnId").parent().attr("backGroupImage");
        if( imgBGUpload == undefined || imgBGUpload == "" || imgBGUpload == "null"){
            $("#staticBackgroundPicForm .imgUpload").remove();
        }else{
            if($("#staticBackgroundPicForm .imgUpload").length > 0){
                $("#staticBackgroundPicForm .imgUpload a").attr("href",imgBGUpload);
                $("#staticBackgroundPicForm .imgUpload a").text(imgBGUpload);
            }else{
                $("#staticBackgroundPicForm").append('<p class="imgUpload col-xs-8">已上传：<a href="'+imgBGUpload+'" target="_blank">'+imgBGUpload+'</a></p>')
            }
        };
        $("#staticBackgroundPicForm .tablesorter").bind('DOMNodeInserted', function(e) {
            $("#staticBackgroundPicForm .imgUpload").remove();
        });
        // 聚焦
        var focusUpload = $("#columnId").parent().attr("focusBGImg");
        if( focusUpload == undefined || focusUpload == "" || focusUpload == "null"){
            $("#focusBackgroundPicForm .imgUpload").remove();
        }else{
            if($("#focusBackgroundPicForm .imgUpload").length > 0){
                $("#focusBackgroundPicForm .imgUpload a").attr("href",focusUpload);
                $("#focusBackgroundPicForm .imgUpload a").text(focusUpload);
            }else{
                $("#focusBackgroundPicForm").append('<p class="imgUpload col-xs-8">已上传：<a href="'+focusUpload+'" target="_blank">'+focusUpload+'</a></p>')
            }
        };
        $("#focusBackgroundPicForm .tablesorter").bind('DOMNodeInserted', function(e) {
            $("#focusBackgroundPicForm .imgUpload").remove();
        });
        // 点击
        var clickUpload = $("#columnId").parent().attr("clickBGImg");
        if( clickUpload == undefined || clickUpload == "" || clickUpload == "null" ){
            $("#clickBackgroundPicForm .imgUpload").remove();
        }else{
            if($("#clickBackgroundPicForm .imgUpload").length > 0){
                $("#clickBackgroundPicForm .imgUpload a").attr("href",clickUpload);
                $("#clickBackgroundPicForm .imgUpload a").text(clickUpload);
            }else{
                $("#clickBackgroundPicForm").append('<p class="imgUpload col-xs-8">已上传：<a href="'+clickUpload+'" target="_blank">'+clickUpload+'</a></p>')
            }
        };
        $("#clickBackgroundPicForm .tablesorter").bind('DOMNodeInserted', function(e) {
            $("#clickBackgroundPicForm .imgUpload").remove();
        });
        // 事件类型
        var selChoose = $("#columnId").parent().attr("ruleval");
        if(selChoose == "TVSignal"){
            $(".selectNum .optionSel").remove();
            $(".selectNum").find("option[value='TVSignal']").prop("selected", true);
        }else if(selChoose == "Container"){
            $(".selectNum .optionSel").remove();
            $(".selectNum").find("option[value='Container']").prop("selected", true);
        }else if( selChoose == undefined || selChoose == ""){
            $(".selectNum .optionSel").remove();
            $(".selectNum option:first").prop("selected", true);
        }else{
            $(".selectNum").find("option[value='"+selChoose+"']").prop("selected", true);
            if($(".selectNum .optionSel").length > 0){
                // $(".selectNum .optionSel").text($("#columnId").parent().attr("rule"));
            }else{
                $(".selectNum").append('<p class="optionSel col-xs-12">已选择：一条数据</p>')
            }
        };
    }

    //栏目鼠标双击
    function double(){
        $(".hdDown .on").attr("id","columnId");
        if($(".selectNum").find("option[value='Container']").length < 1){
            $(".selectNum select").prepend('<option value="Container">本容器</option>');
        }
        $("#columnDetail").modal("show");
        $("#accordionEvent").hide();
        modalReset();
    }

    $(function(){
        // 获取语言
        $.post(base+"/platform/tenant/scene/tenantLanguage", {id:$("#tenantId").val()}, function (data) {
            var language = data.data;//languageDicCode  KEY
            for(var g = 0; g < language.length; g++){
                $("#languageName").append('<label class="col-xs-3 control-label">'+language[g].languageName+'名称：</label>' +
                    '<div class="col-xs-8">' +
                    '<input type="text" id="" name="" value="" class="form-control" placeholder="请输入名称">' +
                    '<input type="hidden" id="'+language[g].languageDicCode+'" value="'+language[g].languageName+'">' +
                    '</div>');
                if(language[g].languageDicCode == "zh-Hans"){
                    $("#languageName input[type='text']").removeAttr("id");
                    $("#zh-Hans").prev().attr("id","editName");
                }else{
                    $("#languageName div").eq(0).find("input[type='text']").attr("id","editName");
                }
            };
        });

        // 颜色设定
        $('#overallBG,#overallColor,#staticBG,#staticColor,#staticFontBack,#focusBG,#focusColor,#focusFontBack,#clickBG,#clickColor,#clickFontBack').colpick({
            layout:'hex',
            submit:0,
            color:'#ffffff',
            onChange:function(hsb,hex,rgb,el,bySetColor) {
                $(el).css('border-color','#'+hex);
                if(!bySetColor) $(el).val(hex);
            }
        }).keyup(function(){
            $(this).colpickSetColor(this.value);
        });

        // 整体编辑
        $("#overallEdit").on("click",function(){
            var dragAuth = $(".drag").attr("isAuthorize");
            var styleTypes = $(".drag").attr("styleTypes");
            if(dragAuth == undefined){
                $("#overallBG,#overallFont,#overallColor").val("");
                $(".allAuthority").find("input[value='1']").prop("checked", true);
                $(".styleTypes").find("input[value='0']").prop("checked", true);
            }else{
                $("#overallBG").val($(".drag").css("background-color"));
                $("#overallFont").val($(".drag").css("font-size"));
                $("#overallColor").val($(".drag").css("color"));
                $(".allAuthority").find("input[value='"+dragAuth+"']").prop("checked", true);
                $(".styleTypes").find("input[value='"+styleTypes+"']").prop("checked", true);
            };
            // 上传图片
            var dragBGUpload = $(".drag").attr("backGroupImage");
            if( dragBGUpload == undefined || dragBGUpload == "" || dragBGUpload == "null"){
                $("#overallBackgroundPicForm .imgUpload").remove();
            }else{
                if($("#overallBackgroundPicForm .imgUpload").length > 0){
                    $("#overallBackgroundPicForm .imgUpload a").attr("href",dragBGUpload);
                    $("#overallBackgroundPicForm .imgUpload a").text(dragBGUpload);
                }else{
                    $("#overallBackgroundPicForm").append('<p class="imgUpload col-xs-8">已上传：<a href="'+dragBGUpload+'" target="_blank">'+dragBGUpload+'</a></p>')
                }
            };
            $("#overallBackgroundPicForm .tablesorter").bind('DOMNodeInserted', function(e) {
                $("#overallBackgroundPicForm .imgUpload").remove();
            });

        });

        //  整体编辑保存
        $("#overall").on("click",function(){
            $("#overallBackgroundPicForm").submit();
            $(".drag").css({"background-color":"#"+$("#overallBG").val() || "","font-size":$("#overallFont").val(),"color":"#"+$("#overallColor").val() || ""});
            $(".drag").attr("styleTypes",$(".styleTypes").find("input[type='radio']:checked").val());  // 风格
            $(".drag").attr("authority",$(".allAuthority").find("input[type='radio']:checked").val());  // 授权
            $("#overallDetail").modal("hide");
        });

        //整体编辑背景图片回调
        $('#overallBackgroundPicForm').ajaxForm({
            dataType: 'json',
            success: function (data, statusText, xhr, form) {
                if (data.code==0) {
                    $.post(base+"/platform/scene/interface/getPicUrl", {id:data.data}, function (dataImg) {
                        // console.log(data.data);
                        $(".drag").css({"background-image":"url("+dataImg.data+")"});
                        $(".drag").attr("backGroupImage",dataImg.data);
                    });
                    $("#tr_"+data.data).remove();
                    $("#div_"+data.data).remove();
                    form.resetForm();
                } else {
                    Toast.error(data.msg);
                }
            }
        });

        // 挂件删除
        $(".pendant").on("click", ".close", function(){
            $(this).parent().hide();
        })

        // 添加栏目
        $("#addHD").click(function(){
            if($(".hdDown").find("li").length < 6){
                $(".hdDown").find("ul").append("<li><p class='colEdit' ondblclick='double()'>请编辑</p><button type='button' class='close columnClose'>×</button></li>");
                $(".bdTop").append("<ul class='firstUl'></ul>");
                $(".bdTop ul.firstUl").hide().removeAttr("id");
                $(".bdTop ul.firstUl").eq(0).show().attr("id","currentTabId");
                $(".hdDown li").find("input").removeClass("on");
                $(".hdDown li").eq(0).find("p").addClass("on");
                // positionElements();
            }else{
                alert("添加的太多了！")
            }
        });

        // tab标签切换
        $(".hdDown ul").on("click","li",function(e){
            $(".bdTop ul.firstUl").removeAttr("id");
            $(".hdDown li").find("p").removeClass("on");
            $(this).find("p").addClass("on");
            num = $(".hdDown li").index(this);
//            $(this).addClass("on").slblings().removeClass("on");
//            console.log($(".hdDown li").index(this));
            $(".bdTop ul.firstUl").hide();
            $(".bdTop ul.firstUl").eq(num).show();/*.slblings().hide()*/
            //  当前添加id
            $(".bdTop ul.firstUl").eq(num).attr("id","currentTabId");
            // 删除标签
            $(".hdDown").on("click", ".columnClose", function(){
//                console.log( $(".bdTop ul").eq(num));
                $(".bdTop ul#currentTabId").remove();
                $(this).parent('li').remove();
//                $(".hdDown li").eq(0).trigger("click");
            });
            //  当选择的不是本容器时，清空相应内容
            if(($(this).attr("rule") == "null" && $(this).attr("ruleval") == "Container") || $(this).attr("rule") == undefined ){
                $(".bdTop ul.firstUl").eq(num).find("p.tishi").remove();
                $("#recommendDrop").attr("data-toggle","dropdown");
            }else{
                $("#recommendDrop").removeAttr("data-toggle");
            }
            // positionElements();
        });

        // 推荐位删除
        $(".bdTop").on("click", ".bitClose", function(){
            var removeIndex = $(this).parent().parent('li').index();
            // console.log(removeIndex);
            gridster.remove_widget( $('ul#currentTabId li').eq(removeIndex) );//删除某个
        })

        // 编辑推荐位
        $(".bdTop").on("click",".edit",function(){
            // var currentId = $(this).attr("id","columnId");
            $(this).attr("id","columnId");
            $(".selectNum").find("option[value='Container']").remove();
            modalReset();
            if($(".colEdit.on").parent().attr("authority") == "0"){
                $("#accordionEvent").hide();
            }else{
                if($(this).parent().attr("authority") == "0"){
                    $("#accordionEvent").hide();
                }else{
                    $("#accordionEvent").show();
                }
            }
        });

        // 栏目推荐位编辑保存
        $("#columnDetail").on("click","#columnEdit",function(){
            if($("#editName").val() == ""){
                alert("请填写名称");
                return;
            };
            //  本容器，电视信号，自定义的列表，已有一条数据
            if($(".selectNum").find("option:selected").val() != "Container" && $("input[name='custom']:checked").val()!= "customList" && $(".selectNum").find("option:selected").val() != "TVSignal" && (!$("tr").hasClass('selected') && $(".selectNum").find(".optionSel").length < 1 )){
                alert("请选择一条事件类型数据");
                return;
            };
            ruleId();
            if($("#staticBackgroundPicForm img").length > 0){
                modalStatic = false;
                $("#staticBackgroundPicForm").submit();
            }
            if($("#focusBackgroundPicForm img").length > 0){
                modalFocus = false;
                $("#focusBackgroundPicForm").submit();
            }
            if($("#clickBackgroundPicForm img").length > 0){
                modalClick = false;
                $("#clickBackgroundPicForm").submit();
            }
            if($("#staticBackgroundPicForm img").length == 0 && $("#focusBackgroundPicForm img").length == 0 && $("#clickBackgroundPicForm img").length == 0){
                editSave();
            }
            // $("#staticBackgroundPicForm,#focusBackgroundPicForm,#clickBackgroundPicForm").submit();
        })

        // 栏目推荐位背景图片回调
        $("#staticBackgroundPicForm").ajaxForm({
            dataType: 'json',
            success: function (data, statusText, xhr, form) {
                if (data.code==0) {
                    $.post(base+"/platform/scene/interface/getPicUrl", {id:data.data}, function (dataImg) {
                        backgImage = dataImg.data;
                        modalStatic = true;
                        editSave();
                    });
                    $("#tr_"+data.data).remove();
                    $("#div_"+data.data).remove();
                    form.resetForm();
                } else {
                    Toast.error(data.msg);
                }
            }
        });
        $("#focusBackgroundPicForm").ajaxForm({
            dataType: 'json',
            success: function (data, statusText, xhr, form) {
                if (data.code==0) {
                    $.post(base+"/platform/scene/interface/getPicUrl", {id:data.data}, function (dataImg) {
                        focusImage = dataImg.data;
                        modalFocus = true;
                        editSave();
                    });
                    $("#tr_"+data.data).remove();
                    $("#div_"+data.data).remove();
                    form.resetForm();
                } else {
                    Toast.error(data.msg);
                }
            }
        });
        $("#clickBackgroundPicForm").ajaxForm({
            dataType: 'json',
            success: function (data, statusText, xhr, form) {
                if (data.code==0) {
                    $.post(base+"/platform/scene/interface/getPicUrl", {id:data.data}, function (dataImg) {
                        clickImage = dataImg.data;
                        modalClick = true;
                        editSave();
                    });
                    $("#tr_"+data.data).remove();
                    $("#div_"+data.data).remove();
                    form.resetForm();
                } else {
                    Toast.error(data.msg);
                }
            }
        });

        // 栏目弹出框取消
        $("#columnDetail").on("click","#columnCancel,#columnClose",function(){
            $(".hdDown p").removeAttr("id");
            $(".bdTop p").removeAttr("id");
        });

        // 保存
        $("#save").click(function() {
            saveSubmit();
            var tenantSceneVo = {
                appUI:category,
                id:uiId,
            }

            // 提交
            $.ajax({
                url: base+"/platform/tenant/scene/editSceneDo",
                type : "POST",
                dataType : "JSON",
                contentType:"application/json;charset=UTF-8",
                data: JSON.stringify(tenantSceneVo),
                success: function (data) {
                    if (data.code==0) {
                        Toast.success(data.msg);
                        setTimeout(function () {
                            document.getElementById("goback").click();
                        }, 10);
                    } else {
                        Toast.error(data.msg);
                    }
                }
            });
        });
    });

    // 保存数据
    function saveSubmit(){
        // 栏目和推荐位的遍历
        var columnList = $(".hdDown").find("p.colEdit");
        var columuSet = [];
        for(var i = 0; i < columnList.length; i++){
            var recomList = $("ul.firstUl").eq(i).find(".liList");
            $("ul.firstUl").eq(i).css({"display":"block","opacity":"0"});
            var recomSet = [];
            // 推荐位循环
            for(var e = 0; e < recomList.length; e++){
                var recomDiv = {
                    "name": recomList.eq(e).attr("nameList") == null ? null : JSON.parse(recomList.eq(e).attr("nameList")),
                    "id": recomList.eq(e).attr("id") || uuid(),
                    "text": "推荐位",
                    "isAuthorize": recomList.eq(e).attr("authority") || "0", //容器是否向下授权1是 0否
                    "corner": recomList.eq(e).attr("corner"),
                    "style":{
                        "width": recomList.eq(e).width()+2,          //宽
                        "height": recomList.eq(e).height()+2,        //高
                        "parentLeft": recomList.eq(e).parent().position().left,     //相对父窗口位置
                        "parentTop": recomList.eq(e).parent().position().top,       //相对父窗口位置
                        "fontSize": recomList.eq(e).css("font-size"),   //字体大小
                        "fontColor":  recomList.eq(e).css("color"),  //文字颜色
                        "backGroupColor":  recomList.eq(e).css("background-color"), //背景颜色
                        "backGroupImage": recomList.eq(e).attr("backGroupImage"),  //背景图片
                        "fontBackGroupColor": recomList.eq(e).attr("staticFontBack")
                    },
                    "onfocusStyle":{
                        "fontSize": recomList.eq(e).attr("focusSize"),   //字体大小
                        "fontColor":  recomList.eq(e).attr("focusColor"),  //文字颜色
                        "backGroupColor":  recomList.eq(e).attr("focusBG"), //背景颜色
                        "backGroupImage":  recomList.eq(e).attr("focusBGImg"), //背景图片
                        "fontBackGroupColor": recomList.eq(e).attr("focusFontBack")
                    },
                    "onclickStyle":{
                        "fontSize": recomList.eq(e).attr("clickSize"),   //字体大小
                        "fontColor":  recomList.eq(e).attr("clickColor"),  //文字颜色
                        "backGroupColor":  recomList.eq(e).attr("clickBG"), //背景颜色
                        "backGroupImage":  recomList.eq(e).attr("clickBGImg"), //背景图片
                        "fontBackGroupColor": recomList.eq(e).attr("clickFontBack")
                    },
                    "sceneEvents": [ //动作
                        {
                            "eventType": "onclick",  //事件类型
                            "sceneAction": "WINDOW", //场景动作 IAMGE,APP,LAUNCHER,HTML,WINDOW,URL,CONTAINER
                            "beforeEvent": null, //事件触发前事件
                            "afterEvent": null,  //事件触发后事件
                            "rule":  recomList.eq(e).attr("rule") == "null" ? recomList.eq(e).attr("ruleVal") : recomList.eq(e).attr("rule") || "",		//源Id  ---跳转类型id 或者文本
                            // "modelName": recomList.eq(e).attr("ruleVal"),    //源类型  article,adv,media,video，imageList   跳转类型input的val
                            "eventID": recomList.eq(e).attr("ruleVal") == undefined ? "": recomList.eq(e).attr("ruleVal")+"Event" //ArticleEvent,AdvEvent,mediaEvent,videoEvent，ImageListEvent  跳转类型input的val +Event
                        }
                    ],
                    "uiClassID": "PanelUI" //面板控件
                };
                recomSet.push(recomDiv);
            }
            $("ul.firstUl").eq(i).css({"display":"none","opacity":"1"});
            $("ul#currentTabId").css({"display":"block","opacity":"1"});

            // 栏目循环
            var columuLi =  columnList.eq(i).parent('li');
            var navList = {
                "name": columuLi.attr("nameList") == null ? null : JSON.parse(columuLi.attr("nameList")),
                "id": columuLi.attr("id") || uuid(),
                "text": "栏目",
                "isAuthorize": columuLi.attr("authority") || "0", //容器是否向下授权0是 1否
                "corner": columuLi.attr("corner"),
                "style": {
                    "fontSize": columuLi.css("font-size") || "",   //字体大小
                    "fontColor": columuLi.css("color"),  //文字颜色
                    "backGroupColor": columuLi.css("background-color"), //背景颜色
                    "backGroupImage": columuLi.attr("backGroupImage"), //背景图片
                    "width": columuLi.width(),          //宽
                    "height": columuLi.height(),        //高
                    "parentLeft": columuLi.position().left,     //相对父窗口位置+117
                    "parentTop": columuLi.position().top     //相对父窗口位置+15
                },
                "onfocusStyle":{
                    "fontSize": columuLi.attr("focusSize"),   //字体大小
                    "fontColor":  columuLi.attr("focusColor"),  //文字颜色
                    "backGroupColor":  columuLi.attr("focusBG"), //背景颜色
                    "backGroupImage":  columuLi.attr("focusBGImg"), //背景图片
                },
                "onclickStyle":{
                    "fontSize": columuLi.attr("clickSize"),   //字体大小
                    "fontColor":  columuLi.attr("clickColor"),  //文字颜色
                    "backGroupColor":  columuLi.attr("clickBG"), //背景颜色
                    "backGroupImage":  columuLi.attr("clickBGImg"), //背景图片
                },
                "sceneEvents": [ //动作
                    {
                        "eventType": "onclick",  //事件类型 onclick/onfocus
                        "sceneAction": "CONTAINER", //场景动作  IAMGE,APP,LAUNCHER,HTML,WINDOW,URL,CONTAINER
                        "beforeEvent": null, //事件触发前事件
                        "afterEvent": null,  //事件触发后事件
                        "rule": columuLi.attr("rule") == "null" && columuLi.attr("ruleVal") == "Container" ? { //容器 ===  推荐位
                            // "name": null,
                            "id": $(".bdTop").attr("id") || uuid(),
                            "text": "推荐位集合",
                            "style": {
                                "width": $(".bdTop").width()+10,          //宽
                                "height": $(".bdTop").height()+11,        //高
                                "parentLeft": $(".bdTop").position().left,     //相对父窗口位置
                                "parentTop": $(".bdTop").position().top       //相对父窗口位置
                            },
                            "subContainer": recomSet,
                            "uiClassID": "PanelUI"
                        } : columuLi.attr("rule"),		//源Id  ---跳转类型id 或者文本

                        // "rule":recomList.eq(e).attr("rule"),
                        "eventID": columuLi.attr("ruleVal")+"Event" //ArticleEvent,AdvEvent,mediaEvent,videoEvent，ImageListEvent  跳转类型input的val +Event

                        // "modelName": "Container"  //容器
                    }
                ],
                "uiClassID": "ColumnUI"  //导航控件
            };
            columuSet.push(navList);
        }

        //  面板
        var subContainer = [
            {// 挂件面板
                // "name": "dragging",
                "id": $(".dragging").attr("id") || uuid(),
                "text": "挂件",
                "isAuthorize": "0", //容器是否向下授权1是 0否
                "style": {
                    "width":$(".dragging").width() ,          //宽 $(".dragging").width()
                    "height": $(".dragging").height()+1,         //高 $(".dragging").height()
                    "parentLeft": $(".dragging").position().left,     //相对父窗口位置
                    "parentTop": $(".dragging").position().top       //相对父窗口位置
                },
                "subContainer": [
                    {// 天气
                        // "name": "weather",
                        "id": $(".weather").attr("id") || uuid(),
                        "text": "天气",
                        "isAuthorize": "0", //容器是否向下授权1是 0否
                        "style": {
                            "width": $(".weather").width(),          //宽
                            "height": $(".weather").height(),         //高
                            "parentLeft": $(".weather").css("display") == "none" ? "-1" : $(".weather").position().left,     //相对父窗口位置
                            "parentTop": $(".weather").position().top       //相对父窗口位置
                        },
                        "pendantType": "time",//挂件类型 time、weather
                        "uiClassID": "PendantUI" //挂件控件
                    },{//日期
                        // "name": "date",
                        "id": $(".date").attr("id") || uuid(),
                        "text": "日期",
                        "isAuthorize": "0", //容器是否向下授权1是 0否
                        "style": {
                            "width": $(".date").width(),          //宽
                            "height": $(".date").height(),         //高
                            "parentLeft": $(".date").css("display") == "none" ? "-1" :  $(".date").position().left,     //相对父窗口位置
                            "parentTop": $(".date").position().top       //相对父窗口位置
                        },
                        "pendantType": "weather",//挂件类型 time、weather
                        "uiClassID": "PendantUI" //挂件控件
                    }
                ],
                "uiClassID": "PanelUI" //面板控件
            },
            { //导航面板
                // "name": "fullSlide",
                "id": $(".fullSlide").attr("id") || uuid(),
                "text": "导航",
                "isAuthorize": "0", //容器是否向下授权1是 0否
                "style": {
                    "width": $(".fullSlide").width(),          //宽
                    "height": $(".fullSlide").height(),         //高
                    "parentLeft": $(".fullSlide").position().left,     //相对父窗口位置
                    "parentTop": $(".fullSlide").position().top        //相对父窗口位置
                },
                "subContainer":columuSet,
                "uiClassID": "PanelUI" //面板控件
            }
        ];

        category = {
            // 整体
            // "name": "drag", //容器名称
            "id": $(".drag").attr("id") || uuid(),
            "text": "容器", //容器文本
            "isAuthorize":$(".drag").attr("authority") || "0", //容器是否向下授权   1是 ，0否
            "style" : {
                "fontSize": $(".drag").css("font-size"),   //字体大小
                "fontFamily": $(".drag").css("font-family"), //字体样式
                "fontColor": $(".drag").css("color"),  //文字颜色
                "backGroupColor": $(".drag").css("background-color"), //背景颜色
                "backGroupImage": $(".drag").attr("backGroupImage"), //背景图片
                "width": $(".drag").width()+2,          //宽 $(".dragging").width()
                "height": $(".drag").height()+2,        //高
                "parentLeft": 0,     //相对父窗口位置
                "parentTop": 0       //相对父窗口位置
//                "styleTypes" : $(".drag").attr("styleTypes")  //风格
            },
            "subContainer":subContainer,
//               "bit" : bit, // 推荐位
            "uiClassID": "AppUI"
        };

    }

    function editSave(){
        if( $(".selectNum").find(".optionSel").length > 0 ){
            chks = $("#columnId").parent().attr("rule");
        }
        // 语言名称
        var nameList = [];
        for(var i = 0; i < $("#languageName div").length; i++){
            var name = {};
            name.key = $("#languageName div").eq(i).find("input[type='hidden']").attr("id");
            name.value =  $("#languageName div").eq(i).find("input[type='text']").val();
            nameList.push(name);
        }
        var nameMap = {};
        for(var s = 0; s < nameList.length; s++){
            nameMap[nameList[s].key] = nameList[s].value;
        }
        // console.log(nameMap);
        //  栏目
        $(".hdDown p").each(function(){
            var columnCur = $(this);
            if(columnCur.attr('id') == 'columnId'){
                // 基本信息
                columnCur.parent("li").attr("nameList",JSON.stringify(nameMap) || null);
                columnCur.text($("input#editName").val());
                columnCur.parent("li").attr("corner",$(".corner").find("option:selected").val());
                columnCur.parent("li").attr("authority",$(".authority").find("input[type='radio']:checked").val());
                // 静态样式
                columnCur.parent("li").css({"background-image":"url("+backgImage+")","background-color":"#"+$("#staticBG").val() || "","color": "#"+$("#staticColor").val() || "","font-size": $("#staticSize").val()});
                columnCur.parent("li").attr("backGroupImage",backgImage);
                // onfocusStyle 容器聚焦样式
                columnCur.parent("li").attr({"focusBGImg": focusImage,"focusBG": $("#focusBG").val(), "focusColor": $("#focusColor").val(), "focusSize": $("#focusSize").val() });
                // onclickStyle 容器点击样式
                columnCur.parent("li").attr({"clickBGImg": clickImage,"clickBG": $("#clickBG").val(), "clickColor": $("#clickColor").val(), "clickSize": $("#clickSize").val() });

                columnCur.parent("li").attr("rule",chks[0].id || chks ); //跳转类型id 或者文本
                columnCur.parent("li").attr("ruleVal",postType ); //跳转类型input的val

                //  当选择的不是本容器时，清空相应内容
                if(columnCur.parent("li").attr("rule") == "null" && columnCur.parent("li").attr("ruleval") == "Container"){
                    $(".bdTop ul.firstUl").eq(num).find("p.tishi").remove();
                    $("#recommendDrop").attr("data-toggle","dropdown");
                }else{
                    $(".bdTop ul.firstUl").eq(num).find("li").remove();
                    $(".bdTop ul.firstUl").eq(num).find(".tishi").remove();
                    $(".bdTop ul.firstUl").eq(num).append('<p class="tishi" style="text-align: center;">跳转资源</p>');
                    $("#recommendDrop").removeAttr("data-toggle");
                }
                if(modalStatic == true && modalFocus == true && modalClick == true){
                    $("#columnDetail").modal("hide");
                    $(".hdDown p").removeAttr("id");
                }
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:colPicId }, function (data) {
                //     columnCur.parent("li").css({"background-image":"url("+data.data+")"});//静态
                // });
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:focusPicId }, function (data) {
                //     columnCur.parent("li").attr({  "focusBGImg": data.data });
                // });
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:clickPicId }, function (data) {
                //     columnCur.parent("li").attr({  "clickBGImg": data.data });
                // });

                // $("#columnDetail").modal("hide");
                // $(".hdDown p").removeAttr("id");
            }
        });
        // 推荐位
        $(".bdTop p").each(function(){
            var current = $(this);
            if(current.attr('id') == 'columnId'){
                // 基本信息
                current.parent(".liList").attr("nameList",JSON.stringify(nameMap) || null);
                current.text($("input#editName").val());
                current.parent(".liList").attr("corner",$(".corner").find("option:selected").val());
                current.parent(".liList").attr("authority",$(".authority").find("input[type='radio']:checked").val());
                // 静态样式
                current.parent(".liList").css({"background-image":"url("+backgImage+")","background-color":"#"+$("#staticBG").val() || "","color":"#"+$("#staticColor").val() || "","font-size": $("#staticSize").val()});
                current.parent(".liList").attr({"backGroupImage":backgImage,"staticFontBack":$("#staticFontBack").val()});
                current.css({"background-color": "#"+$("#staticFontBack").val(),"color":"#"+$("#staticColor").val() });
                // onfocusStyle 容器聚焦样式
                current.parent(".liList").attr({"focusBGImg": focusImage,"focusBG": $("#focusBG").val(), "focusColor": $("#focusColor").val(), "focusSize": $("#focusSize").val(),"focusFontBack":$("#focusFontBack").val() });
                // onclickStyle 容器点击样式
                current.parent(".liList").attr({"clickBGImg": clickImage,"clickBG": $("#clickBG").val(), "clickColor": $("#clickColor").val(), "clickSize": $("#clickSize").val(),"clickFontBack":$("#clickFontBack").val() });

                current.parent(".liList").attr("rule",chks[0].id || chks ); //跳转类型id 或者文本
                current.parent(".liList").attr("ruleVal",postType ); //跳转类型input的val

                if(modalStatic == true && modalFocus == true && modalClick == true){
                    $("#columnDetail").modal("hide");
                    $(".bdTop p").removeAttr("id");
                }
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:colPicId }, function (data) {
                //     var dataList = data.data;
                //     if(current.parent(".liList").find("img").length > 0){
                //         $(".slideBox").remove();
                //     }
                //     if(backgImage.length > 0){
                //         if(current.parent(".liList").find("img").length > 0){
                //             current.parent(".liList").find("img").attr("src",backgImage);
                //         }else{
                //             current.parent(".liList").append('<img src="'+backgImage+ '" width="100%" height="100%" alt="">');
                //         }
                //     }
                // });
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:focusPicId }, function (data) {
                //     current.parent(".liList").attr({ "focusBGImg": data.data });
                // });
                // 回显图片
                // $.post(base+"/platform/scene/interface/getPicUrl", {id:clickPicId }, function (data) {
                //     current.parent(".liList").attr({ "clickBGImg": data.data });
                // });
                // $("#columnDetail").modal("hide");
                // $(".bdTop p").removeAttr("id");
            }
        })
    }

    // function saveFeaturedFirst(){
    //     $(".bdTop p").each(function(){
    //         var current = $(this);
    //         if(current.attr('id') == 'columnId'){
    //             current.text($("input#recName").val());//填充内容
    //             current.parent(".liList").css({"background":$("#recBG").val()});//填充内容
    //             var authorityNum = $(".authority").find("input[type='radio']:checked").val();
    //             current.parent(".liList").attr("authority",authorityNum);
    //             current.parent(".liList").attr("rule",chks[0].id || chks ); //跳转类型id 或者文本
    //             current.parent(".liList").attr("ruleVal",postType ); //跳转类型input的val
    //
    //             // 回显图片
    //             $.post(base+"/platform/scene/interface/getPicUrl", {id:picId }, function (data) {
    //
    //                 var dataList = data.data;
    //                 if(current.parent(".liList").find("img").length > 0){
    //                     $(".slideBox").remove();
    //                 }
    //
    //                 if(dataList.length > 0){
    //                     if(current.parent(".liList").find("img").length > 0){
    //                         current.parent(".liList").find("img").attr("src",data.data);
    //                     }else{
    //                         current.parent(".liList").append('<img src="'+data.data+ '" width="100%" height="100%" alt="">');
    //                     }
    //                 }
    //             });
    //
    //             $("#recommendDetail").modal("hide");
    //             $(".bdTop p").removeAttr("id");
    //         }
    //     })
    //
    // }
