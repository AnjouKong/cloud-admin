//  页面回显

$(function(){
    //监听粘贴事件
    $('#contents').bind('paste',function(e){
        var pastedText = undefined;
        if (window.clipboardData && window.clipboardData.getData) { // IE
            pastedText = window.clipboardData.getData('Text');
        } else {
            pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
        }
        $("#contents").text(pastedText);

    });
    // 数据文本
    $("#dataText").on("click", function(){
        saveSubmit();
        var dataTextSave = {
            appUI: category
        };
        $("#contents").text(JSON.stringify(dataTextSave, null, 4))
    });
    $("#textSave").on("click",function(){
        var dataTextCopy = eval("(" + $("#contents").text() + ")");
        var textData = dataTextCopy.appUI;
        // console.log(textData);
        $(".bdTop").find("ul").remove();
        $(".hdDown ul").find("li").remove();
        dataEcho(textData);
        $("#textDetail").modal("hide");
    });
})
var uiId = "";
$.post(base+"/platform/tenant/scene/getSceneJson", {id:$("#tenantSceneId").val()}, function (data) {
    uiId = data.data.id;
    var data = data.data.appUI;
    dataEcho(data);
});

// 复制
function textCopy(){
    var Url2 = document.getElementById("contents");
    Url2.select(); // 选择对象
    document.execCommand("Copy"); // 执行浏览器复制命令
    Toast.success("已复制好，可贴粘。");
}

function dataEcho(data){
    // console.log(data)
    if(data != null){
        var dragStyle = data.style; // 容器drag的样式
        $(".drag").css({
            "height":dragStyle.height,
            "width":dragStyle.width,
            "top":"0",
            "left":"0",
            "font-size":dragStyle.fontSize,
            "font-family":dragStyle.fontFamily,
            "color":dragStyle.fontColor,
            "background-color":dragStyle.backGroupColor,
            "background-image":"url("+dragStyle.backGroupImage+")"
        });
        $(".drag").attr({"id":data.id,"isAuthorize":data.isAuthorize,"backGroupImage":dragStyle.backGroupImage});
        var subContainer = data.subContainer;
        for(var s = 0; s < subContainer.length; s++){
            if(subContainer[s].subContainer[0].uiClassID == "PendantUI"){  //挂件
                // console.log("这个是挂件");
                var draggStyle = subContainer[s].style; // 挂件父级dragging的样式
                $(".dragging").css({"height":draggStyle.height,"width":draggStyle.width,"top":draggStyle.parentTop,"left":draggStyle.parentLeft});
                $(".dragging").attr("id",subContainer[s].id);
                var dragSub = subContainer[s].subContainer;
                for(var d = 0; d < dragSub.length; d++){
                    var dragSubStyle = dragSub[d].style;
                    if(dragSub[d].text == "天气" && dragSubStyle.parentLeft >= 0 && dragSubStyle.parentTop >= 0){
                        $(".dragging .weather").css({"height":dragSubStyle.height,"width":dragSubStyle.width,"top":dragSubStyle.parentTop+"px","left":dragSubStyle.parentLeft+"px","display":"block" });
                        $(".dragging .weather").attr("id",dragSub[d].id);
                    };
                    if(dragSub[d].text == "日期" && dragSubStyle.parentLeft >= 0 && dragSubStyle.parentTop >= 0){
                        $(".dragging .date").css({"height":dragSubStyle.height,"width":dragSubStyle.width,"top":dragSubStyle.parentTop+"px","left":dragSubStyle.parentLeft+"px","display":"block" });
                        $(".dragging .date").attr("id",dragSub[d].id);
                    }
                };
            }else if(subContainer[s].subContainer[0].uiClassID == "ColumnUI"){
                // console.log("这个是导航");
                var fullStyle = subContainer[s].style; // 导航父级fullSlide的样式
                $(".fullSlide").css({"height":fullStyle.height,"width":fullStyle.width,"top":"0","left":"0"});
                $(".fullSlide").attr("id",subContainer[s].id);
                var fullSub = subContainer[s].subContainer;
                for(var f = 0; f < fullSub.length; f++){
                    var columnStyle = fullSub[f].style; // 挂件父级hdDown li的样式
                    var columnFocus = fullSub[f].onfocusStyle; // 聚焦
                    var columnClick = fullSub[f].onclickStyle; // 点击
                    var columnRule = (fullSub[f].sceneEvents[0].rule.id) == undefined ? fullSub[f].sceneEvents[0].rule : "null"; // 栏目事件类型：本容器 = null，其他对应id
                    //  多语言name 对象
                    var fullName = fullSub[f].name;
                    var disName;
                    for(var key in fullName){
                        if(key == "zh-Hans"){
                            disName = fullName[key];
                            break;
                        }else{
                            disName = fullName[key];
                        }
                    }
                    $(".hdDown").find("ul").append("<li id='"+fullSub[f].id+"' style='font-size:"+columnStyle.fontSize+";color:"+columnStyle.fontColor+";background-color:"+columnStyle.backGroupColor+";background-image:url("+ columnStyle.backGroupImage+");width:"+columnStyle.width+"px;height:"+columnStyle.height+"px;top:"+columnStyle.parentTop+";left:"+columnStyle.parentLeft+";' " +
                        "authority='"+fullSub[f].isAuthorize+"' corner='"+fullSub[f].corner+"' backGroupImage='"+columnStyle.backGroupImage+"' focusBG='"+columnFocus.backGroupColor+"' focusColor='"+columnFocus.fontColor+"' focusBGImg='"+columnFocus.backGroupImage+"' focusSize='"+columnFocus.fontSize+"' " +
                        "clickBG='"+columnClick.backGroupColor+"' clickColor='"+columnClick.fontColor+"' clickBGImg='"+columnClick.backGroupImage+"' clickSize='"+columnClick.fontSize+"' " +
                        "rule='"+columnRule+"' ruleVal='"+(fullSub[f].sceneEvents[0].eventID).split("Event")[0]+"' nameList='"+JSON.stringify(fullName)+"' >" +
                        "<p class='colEdit' ondblclick='double()'>"+disName+"</p>" +
                        "</li>");
                    $(".bdTop").append("<ul class='firstUl' id='curr'></ul>");

                    if(fullSub[f].sceneEvents[0].rule.id){
                        var sceneStyle = fullSub[f].sceneEvents[0].rule.style; // 推荐位集合bdTop
                        $(".bdTop").css({"height":sceneStyle.height,"width":sceneStyle.width,"top":sceneStyle.parentTop,"left":sceneStyle.parentLeft});
                        $(".bdTop").attr("id", fullSub[f].sceneEvents[0].rule.id);
                        var sceneList = fullSub[f].sceneEvents[0].rule.subContainer; // 推荐位遍历
                        for(var e = 0; e < sceneList.length; e++){
                            var sceneLiList = sceneList[e].style; // 静态
                            var sceneFocus = sceneList[e].onfocusStyle; // 聚焦
                            var sceneClick = sceneList[e].onclickStyle; // 点击
                            var sceneRule = (sceneList[e].sceneEvents[0]) == undefined ? "" : sceneList[e].sceneEvents[0].rule;
                            var sceneEventID = (sceneList[e].sceneEvents[0]) == undefined ? "" : (sceneList[e].sceneEvents[0].eventID).split("Event")[0];
                            var sceneLiWidth = parseInt(sceneLiList.width) + 14;
                            var sceneLiHeight = parseInt(sceneLiList.height) + 14;
                            //  多语言name 对象
                            var sceneNames = sceneList[e].name;
                            var disSceneName = "";
                            for(var keys in sceneNames){
                                if(keys == "zh-Hans"){
                                    disSceneName = sceneNames[keys];
                                    break;
                                }else{
                                    disSceneName = sceneNames[keys];
                                }
                            }
                            $(".bdTop").find("ul#curr").append(
                                "<li class='firstLi' style='width:" + sceneLiWidth + "px;height:" + sceneLiHeight + "px;left:" +sceneLiList.parentLeft+ "px;top:" +sceneLiList.parentTop+ "px;'>" +
                                "<div class='liList' id='"+sceneList[e].id+"' style='font-size:"+sceneLiList.fontSize+";color:"+sceneLiList.fontColor+";background-color:"+sceneLiList.backGroupColor+";background-image:url("+sceneLiList.backGroupImage+");width:"+sceneLiList.width+"px; height:"+sceneLiList.height+"px;left: 7px; top: 7px; position:absolute;'" + // border:1px solid gray;
                                "authority='"+sceneList[e].isAuthorize+"' corner='"+sceneList[e].corner+"' backGroupImage='"+sceneLiList.backGroupImage+"' staticFontBack='"+sceneLiList.fontBackGroupColor+"' focusBG='"+sceneFocus.backGroupColor+"' focusColor='"+sceneFocus.fontColor+"' focusBGImg='"+sceneFocus.backGroupImage+"' focusSize='"+sceneFocus.fontSize+"' focusFontBack='"+sceneFocus.fontBackGroupColor+"' " +
                                "clickBG='"+sceneClick.backGroupColor+"' clickColor='"+sceneClick.fontColor+"' clickBGImg='"+sceneClick.backGroupImage+"' clickSize='"+sceneClick.fontSize+"' clickFontBack='"+sceneClick.fontBackGroupColor+"' " +
                                "rule='"+sceneRule+"' ruleVal='"+sceneEventID+"' nameList='"+JSON.stringify(sceneNames)+"' >"+
                                "<p class='edit' style='background-color:#"+sceneLiList.fontBackGroupColor+"; color:"+sceneLiList.fontColor+"' data-toggle='modal' data-target='#columnDetail' data-backdrop='static' data-keyboard='false'>"+disSceneName+"</p>"+
                                // '<img src="'+sceneList[e].style.backGroupImage+ '" width="100%" height="100%" alt="">'+
                                "</div></li>");

                            disSceneName = "";
                        }
                    }else{
                        $(".bdTop").find("ul#curr").append('<p class="tishi" style="text-align: center;">跳转资源</p>');
                    }
                    $("ul.firstUl").removeAttr("id");
                }
                $(".hdDown li:first").trigger("click");

                // $(".hdDown ul").on("click","li",function(e){
                //     positionElements();
                // });
                // $(".hdDown li").eq(0).find("p").addClass("on");
                // $(".bdTop").find("ul:first").show();
                // $(".bdTop").find("ul:first").attr("id","currentTabId");
            }
        }
    }
}

