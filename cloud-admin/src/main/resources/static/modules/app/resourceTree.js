function initSecondaryTreeView() {
    $("#jsTreeUnit_Secondary").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=secondary" : base+"/platform/cms/channel/tree?type=secondary&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
            $("#channelId_Secondary").val("");
        }else{
            $("#channelId_Secondary").val(id);
        }

        if (dataTableSecondary) {
            $(".cd-panel-content").find("input").val("");
            dataTableSecondary.ajax.reload();
        } else {
            initDataTableSecondary();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initImgTreeView() {
    $("#jsTreeUnit_img").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=img" : base+"/platform/cms/channel/tree?type=img&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
            $("#channelId_img").val("");
        }else{
            $("#channelId_img").val(id);
        }

        if (dataTableImg) {
            $(".cd-panel-content").find("input").val("");
            dataTableImg.ajax.reload();
        } else {
            initDataTableImg();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initHtmlTreeView() {
    $("#jsTreeUnit_html").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=html" : base+"/platform/cms/channel/tree?type=html&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
        $("#channelId_html").val("");
        }else{
        $("#channelId_html").val(id);
        }

        if (dataTableArticle) {
            $(".cd-panel-content").find("input").val("");
            dataTableArticle.ajax.reload();
        } else {
            initDataTableArticle();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initAppTreeView() {
    $("#jsTreeUnit_app").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=app" : base+"/platform/cms/channel/tree?type=app&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
        $("#channelId_app").val("");
        }else{
        $("#channelId_app").val(id);
        }

        if (dataTableApp) {
            $(".cd-panel-content").find("input").val("");
            dataTableApp.ajax.reload();
        } else {
            initDataTableApp();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initImgListTreeView() {
    $("#jsTreeUnit_imgList").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=imgCollection" : base+"/platform/cms/channel/tree?type=imgCollection&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
        $("#channelId_imgList").val("");
        }else{
        $("#channelId_imgList").val(id);
        }

        if (dataTableImgCollection) {
            $(".cd-panel-content").find("input").val("");
            dataTableImgCollection.ajax.reload();
        } else {
            initDataTableImgCollection();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initVideoTreeView() {
    $("#jsTreeUnit_video").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=video" : base+"/platform/cms/channel/tree?type=video&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
        $("#channelId_video").val("");
        }else{
        $("#channelId_video").val(id);
        }

        if (dataTableVideo) {
            $(".cd-panel-content").find("input").val("");
            dataTableVideo.ajax.reload();
        } else {
            initDataTableVideo();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

function initAdvTreeView() {
    $("#jsTreeUnit_adv").jstree({
        plugins: ["wholerow"],
        core: {
            data: {
                url: function (node) {
                    return node.id === "#" ? base+"/platform/cms/channel/tree?type=adv" : base+"/platform/cms/channel/tree?type=adv&pid=" + node.id
                }
            },
            multiple: false
        }
    }).on("select_node.jstree", function (node, selected) {
        var id = selected.selected;
        if(id=="root"){
        $("#channelId_adv").val("");
        }else{
        $("#channelId_adv").val(id);
        }

        if (dataTableAdv) {
            $(".cd-panel-content").find("input").val("");
            dataTableAdv.ajax.reload();
        } else {
            initDataTableAdv();
        }
    }).on("loaded.jstree", function (node, jstree) {
        $(node.target).find("li:first div").addClass("jstree-wholerow-clicked");
    });
}

$(function () {
    initSecondaryTreeView();
    initImgTreeView();
    initHtmlTreeView();
    initAppTreeView();
    initImgListTreeView();
    initVideoTreeView();
    initAdvTreeView();
});
