//容器样式
function UiStyle(width, height, parentLeft, parentTop, backGroupImage, fontSize, fontFamily, fontColor, backGroupColor, fontBackGroupColor) {
    this.width = width;
    this.height = height;
    this.parentLeft = parentLeft;
    this.parentTop = parentTop;
    this.backGroupImage = backGroupImage;
    this.fontSize = fontSize;
    this.fontFamily = fontFamily;
    this.backGroupColor = backGroupColor;
    this.fontBackGroupColor = fontBackGroupColor;
}

//事件
function UiEventUiEvent(eventType, sceneAction, beforeEvent, afterEvent, rule, model, event) {
    this.eventType = eventType;
    this.sceneAction = sceneAction;
    this.beforeEvent = beforeEvent;
    this.afterEvent = afterEvent;
    this.rule = rule;
    this.model = model;
    this.event = event;
}

//容器
function Container(id, name, text, isAuthorize, uiStyle, uiEvent, subContainer) {
    this.id = id;
    this.name = name;
    this.text = text;
    this.isAuthorize = isAuthorize;
    this.style = uiStyle;
    this.sceneEvents = uiEvent;
    this.subContainer = subContainer;
}

//栏目
function CategoryUI(id, name, text, isAuthorize, uiStyle, uiEvent, subContainer) {
    Container.call(this, id, name, text, isAuthorize, uiStyle, uiEvent, subContainer);
    this.uiClass = "ColumnUI";
}

//面板
function PanelUI(id, name, text, isAuthorize, uiStyle, uiEvent, subContainer) {
    Container.call(this, id, name, text, isAuthorize, uiStyle, uiEvent, subContainer);
    this.uiClass = "PanelUI";
}

//挂件
function PendantUI(id, name, text, isAuthorize, uiStyle, uiEvent, subContainer, pendantType) {
    Container.call(this, id, name, text, isAuthorize, uiStyle, uiEvent, subContainer);
    this.pendantType = pendantType;
    this.uiClass = "PendantUI";
}

//html控件
function HtmlUI(id, name, text, isAuthorize, uiStyle, uiEvent, subContainer) {
    Container.call(this, id, name, text, isAuthorize, uiStyle, uiEvent, subContainer);
    this.uiClass = "HtmlUI";
}
//推荐位
var bdTop = $(".bdTop");
var gridster;
function positionElements(){
    $("#addBD1,#addBD2,#addBD3,#addBD4,#addBD5,#addBD6").unbind();
    gridster = $(".gridster ul#currentTabId").gridster({        //通过jquery选择DOM实现gridster
        widget_margins: [0,0],   //模块的间距 [上下,左右]
        widget_base_dimensions: [194, 143] //模块的宽高 [宽,高]
    }).data('gridster');
    var p = this;
    $("#addBD1").bind("click", {widthParent: "194px", heightParent: "143px", width: "100% - 14px", height: "100% - 14px", sizex: "1", sizey: "1"}, p.addEvent);
    $("#addBD2").bind("click", {widthParent: "194px", heightParent: "286px", width: "100% - 14px", height: "100% - 14px", sizex: "1", sizey: "2"}, p.addEvent);
    $("#addBD3").bind("click", {widthParent: "388px", heightParent: "143px", width: "100% - 14px", height: "100% - 14px", sizex: "2", sizey: "1"}, p.addEvent);
    $("#addBD4").bind("click", {widthParent: "388px", heightParent: "286px", width: "100% - 14px", height: "100% - 14px", sizex: "2", sizey: "2"}, p.addEvent);
    $("#addBD5").bind("click", {widthParent: "582px", heightParent: "286px", width: "100% - 14px", height: "100% - 14px", sizex: "3", sizey: "2"}, p.addEvent);
    $("#addBD6").bind("click", {widthParent: "776px", heightParent: "286px", width: "100% - 14px", height: "100% - 14px", sizex: "4", sizey: "4"}, p.addEvent);
}
function addEvent(style){
        if($(".hdDown li").length > 0){
            var addUI = '<li class="firstLi" style="width:' + style.data.widthParent + ';height:' + style.data.heightParent + ';" data-row="1" data-col="1" data-sizex="' + style.data.sizex + '" data-sizey="' + style.data.sizey + '">' +
                '<div class="liList" style="width:calc(' + style.data.width + '); height:calc(' + style.data.height + '); border:1px solid gray; position:absolute;left: 7px; top: 7px; " >'+
                '<button type="button" class="close bitClose">×</button>'+
                '<p class="edit" data-toggle="modal" data-target="#columnDetail" data-backdrop="static" data-keyboard="false">请编辑</p>'+
                '</div></li>';
            gridster.add_widget(addUI,style.data.sizex,style.data.sizey);
            // bdTop.find("ul#currentTabId").append(addUI);
        }else{
            alert("请先添加栏目！");
            return;
        }

}
//挂件
var pendant = $(".pendant");
$(function () {
    //工具集------------------------------

    //画布    var canvas = $(".fullSlide");

    // var bdTop = $(".bdTop");
    // var positionElements = {
    //     init: function () {
    //         var p = this;
    //         $("#addBD1").bind("click", {calc: "25% - 14px", height: "49%"}, p.addEvent);
    //         $("#addBD2").bind("click", {calc: "25% - 14px", height: "100%"}, p.addEvent);
    //         $("#addBD3").bind("click", {calc: "50% - 14px", height: "49%"}, p.addEvent);
    //         $("#addBD4").bind("click", {calc: "50% - 14px", height: "100%"}, p.addEvent);
    //         $("#addBD5").bind("click", {calc: "75% - 14px", height: "100%"}, p.addEvent);
    //         $("#addBD6").bind("click", {calc: "100% - 14px", height: "100%"}, p.addEvent);
    //     },
    //     addEvent: function (style) {
    //         var addUI = '<li class="firstLi"><div class="liList" style="width:calc(' + style.data.calc + '); height:' + style.data.height + '; border:1px solid gray; position:absolute; left: 7px; top: 0;" >'+
    //             '<button type="button" class="close bitClose">×</button>'+
    //             '<p class="edit" data-toggle="modal" data-target="#columnDetail" data-backdrop="static" data-keyboard="false">请编辑</p>'+
    //             '</div></li>';
    //         bdTop.find("ul#currentTabId").append(addUI);
    //     }
    // };
    // positionElements.init();

    //挂件
    var pendantElements = {
        init: function () {
            var wd = this;
            $("#addDate1").bind("click", {left: "0px", top: "15px"}, wd.dateEvent);
            $("#addDate2").bind("click", {left: "199px", top: "15px"}, wd.dateEvent);
            $("#addDate3").bind("click", {left: "398px", top: "15px"}, wd.dateEvent);
            $("#addDate4").bind("click", {left: "597px", top: "15px"}, wd.dateEvent);
            $("#addWeather1").bind("click", {left: "0px", top: "15px"}, wd.weatherEvent);
            $("#addWeather2").bind("click", {left: "199px", top: "15px"}, wd.weatherEvent);
            $("#addWeather3").bind("click", {left: "398px", top: "15px"}, wd.weatherEvent);
            $("#addWeather4").bind("click", {left: "597px", top: "15px"}, wd.weatherEvent);
        },
        weatherEvent: function (w) {
            if($(".date").position().left + "px" == w.data.left && $(".date").css("display") != "none"){
                alert("位置重复，请重新选择！");
                return;
            }else{
                $(".weather").show();
                $(".weather").css({"left": w.data.left,"top": w.data.top,"position":"absolute"});
            }
        },
        dateEvent: function (d) {
            console.log(d.data.left)
            if($(".weather").position().left + "px" == d.data.left && $(".date").css("display") != "none"){
                alert("位置重复，请重新选择！");
                return;
            }else{
                $(".date").show();
                $(".date").css({"left": d.data.left,"top": d.data.top,"position":"absolute"})
            }
        }
    };
    pendantElements.init();
    // $(".pendant").on({
    //     mousedown: function (event) {
    //         $("." + event.target.id).show();
    //         $("." + event.target.id).css({"top":"15px","left":"0px","position":"absolute"});
    //         $(".date").css({"left":"197px"});
    //         // $("." + event.target.id).draggable({containment: ".dragging", scroll: false});
    //     }
    // }, "#weather,#date");



});