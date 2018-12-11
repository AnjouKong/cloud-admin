
var category;
$(document).ready(function () {
    // 添加标签
    $("#addTabData").on('click', function () {
        var tabLast = $(".secondaryList .tabContent:last").index();
        var cloneData = $(".cloneTemplate").html();
        cloneData = cloneData.replace(new RegExp("tabImg","gm"), "tabImg"+ parseInt(tabLast + 2));
        // cloneData = cloneData.replace(new RegExp("tabUploadForm","gm"), "tabUploadForm"+ parseInt(tabLast + 2));
        cloneData = cloneData.replace(new RegExp("itemImg","gm"), "itemImg"+ parseInt(tabLast + 2));
        // cloneData = cloneData.replace(new RegExp("itemUploadForm","gm"), "itemUploadForm"+ parseInt(tabLast + 2));
        $(".secondaryList").append(cloneData);
        publicMethod();
    });
    // 添加内容
    $("#addItemData").on('click', function () {
        if($(".selected").length > 0){
            var itemLast = $(".selected .tabItem:last").index();
            var tabSelected = $(".secondaryList .selected").index();
            var itemCloneData = $(".cloneTemplate .tabContent li:nth-child(3)").html();
            itemCloneData = itemCloneData.replace(new RegExp("itemImg","gm"), "itemImg"+ tabSelected + parseInt(itemLast + 2));
            // itemCloneData = itemCloneData.replace(new RegExp("itemUploadForm","gm"), "itemUploadForm"+ tabSelected + parseInt(itemLast + 2));
            $(".selected li:nth-child(3)").append(itemCloneData);
        }else{
            Toast.error("先选择添加的标签！");
        }
    });
    // 删除标签
    $("#deleteTab").on('click', function () {
        if($(".secondaryList .tabContent").length < 2){
            Toast.error("请至少保留一条！");
            return;
        }
        if($(".selected").length > 0 ){
            var dialog = $("#dialogDeleteCheck");
            dialog.modal("show");
            dialog.find("#ok").unbind("click");
            dialog.find("#ok").bind("click", function () {
                $(".selected").remove();
                dialog.modal("hide");
            });
        }else{
            Toast.error("先选择删除的标签！");
        }
    });
    // 保存数据
    $("#save").on("click", function() {
        saveSubmit();
        var sceneSaveVo = {
            id: $("#secondaryId").val(),
            content: category, //JSON.stringify(category)
        };
        // 提交
        $.ajax({
            url: base+"/platform/cms/secondary/configDo",
            type : "POST",
            dataType : "JSON",
            contentType:"application/json;charset=UTF-8",
            data: JSON.stringify(sceneSaveVo),
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
    // 获取回显数据
    $.post(base+"/platform/cms/secondary/detail", {id:$("#secondaryId").val()}, function (data) {
        var data = data.data;
        if(data){
            dataEcho(JSON.parse(data));
            // setTimeout(function(){},1000);
        }else{
            $("#addTabData").trigger("click");
        }
    });
});

function publicMethod(){
    $(".secondaryList").on('click', 'ul.tabContent', function () {
        $('ul.tabContent').removeClass('selected');
        $(this).addClass('selected');
    });
    $(".deleteUpload").on('click', function () {
        $(this).parent("p").remove();
    });
    $(".deleteItem").on('click', function () {
        var _this = $(this);
        if(_this.parents("li").find(".tabItem").length > 1){
            var dialog = $("#dialogDeleteCheck");
            dialog.modal("show");
            dialog.find("#ok").unbind("click");
            dialog.find("#ok").bind("click", function () {
                _this.parent(".tabItem").remove();
                dialog.modal("hide");
            });
        }else{
            Toast.error("请至少保留一条！");
            return;
        }

    });
};

function saveSubmit(){
    // 语言
    var languageList = $(".secondaryLanguage");
    var languageData = [];
    for(var l = 0; l < languageList.length; l++){
        // 标签
        var tabList = $(".secondaryList").find(".tabContent");
        var tabData = [];
        for(var t = 0; t < tabList.length; t++){
            // 内容
            var itemList = tabList.eq(t).find(".tabItem");
            var itemData = [];
            for(var i = 0; i < itemList.length; i++){
                var itemListData = {
                    contentImg: itemList.eq(i).find(".itemUpload input[type='hidden']").attr("name") || itemList.eq(i).find(".itemUpload p a").attr("href"),  // 右侧内容的图片
                    content: {   // 右侧内容描述
                        title: (itemList.eq(i).find("input.itemName"))[l].value,
                        info: (itemList.eq(i).find("input.itemInfo"))[l].value,
                    }
                };
                itemData.push(itemListData);
            };
            var tabListData = {
                itemName: (tabList.eq(t).find("input.tabName"))[l].value, // 左侧标签的名字
                itemImg: tabList.eq(t).find("li:nth-child(1) input[type='hidden']").attr("name") || tabList.eq(t).find("li:nth-child(1) p a").attr("href"),  // 左侧标签的图片
                itemData: itemData,
            };
            tabData.push(tabListData);
        };
        var languageListData = {
            title: $(".totalTitle input")[l].value, // 整体标题
            language: languageList[l].value,
            data: tabData,
        };
        languageData.push(languageListData);
    };
    category = {
        type: $("#secondaryStyle").val(), // 风格
        languageData: languageData,
    };
    // console.log(category);
}

function dataEcho(data){
    // console.log(data.languageData);
    function languageEcho(mark, tabIndex, itemIndex) {
        var languageList = data.languageData;
        for(var l = 0; l < languageList.length; l++){
            if(mark == "title"){ // 大标题
                var totalTitle = $(".totalTitle > div");
                for(var a = 0; a < totalTitle.length; a++){
                    if(languageList[l].language == totalTitle.eq(a).find("label").text()){
                        totalTitle.eq(a).find("input").val(languageList[l].title);
                    }
                }
            }
            if(mark == "tab"){ // 标签
                var tabTitle = $(".secondaryList .tabContent:last").find("li:nth-child(2) > div");
                for(var b = 0; b < tabTitle.length; b++){
                    if(languageList[l].language+ "：" == tabTitle.eq(b).find("span").text()){
                        tabTitle.eq(b).find("input").val(languageList[l].data[tabIndex].itemName);
                    }
                }
            }
            if(mark == "item"){ // 内容
                var itemName = $(".secondaryList .tabContent:last").find(".tabItem:last .itemTitle > div");
                var itemInfo = $(".secondaryList .tabContent:last").find(".tabItem:last .itemContent > div");
                for(var c = 0; c < itemName.length; c++){
                    if(languageList[l].language+ "：" == itemName.eq(c).find("span").text()){
                        itemName.eq(c).find("input").val(languageList[l].data[tabIndex].itemData[itemIndex].content.title);
                    }
                }
                for(var d = 0; d < itemInfo.length; d++){
                    if(languageList[l].language+ "：" == itemInfo.eq(d).find("span").text()){
                        itemInfo.eq(d).find("input").val(languageList[l].data[tabIndex].itemData[itemIndex].content.info);
                    }
                }
            }
        }
    }
    languageEcho("title");
    // 标签结构
    var tabContentList = data.languageData[0].data;
    for(var t = 0; t < tabContentList.length; t++){
        var tabLast = $(".secondaryList .tabContent:last").index();
        var cloneData = $(".cloneTemplate").html();
        cloneData = cloneData.replace(new RegExp("tabImg","gm"), "tabImg"+ parseInt(tabLast + 2));
        cloneData = cloneData.replace(new RegExp("itemImg","gm"), "itemImg"+ parseInt(tabLast + 2));
        $(".secondaryList").append(cloneData);
        $(".secondaryList .tabContent:last").find(".tabItem").remove();
        if(tabContentList[t].itemImg){
            $(".secondaryList .tabContent:last").find("li:first").append(
                '<p class="imgUpload">已上传：<br />' +
                '<a href="'+tabContentList[t].itemImg+'" title="'+tabContentList[t].itemImg+'" target="_blank">'+tabContentList[t].itemImg+'</a>' +
                '<br /><button type="button" class="deleteUpload">删除图片</button>' +
                '</p>'
            );
        }
        languageEcho("tab", t);
        // 内容结构
        var itemList = tabContentList[t].itemData;
        for(var i = 0; i < itemList.length; i++){
            var itemLast = $(".secondaryList .tabContent:last .tabItem:last").index();
            var tabLast = $(".secondaryList .tabContent:last").index();
            var itemCloneData = $(".cloneTemplate .tabContent li:nth-child(3)").html();
            itemCloneData = itemCloneData.replace(new RegExp("itemImg","gm"), "itemImg"+ parseInt(tabLast + 2) + parseInt(itemLast + 2));
            $(".secondaryList .tabContent:last li:nth-child(3)").append(itemCloneData);
            if(itemList[i].contentImg){
                $(".secondaryList .tabContent:last").find(".tabItem:last .itemUpload").append(
                    '<p class="imgUpload">已上传：<br />' +
                    '<a href="'+itemList[i].contentImg+'" title="'+itemList[i].contentImg+'" target="_blank">'+itemList[i].contentImg+'</a>' +
                    '<br /><button type="button" class="deleteUpload">删除图片</button>' +
                    '</p>'
                );
            }
            languageEcho("item", t, i);
        }
    }
    publicMethod();
}
