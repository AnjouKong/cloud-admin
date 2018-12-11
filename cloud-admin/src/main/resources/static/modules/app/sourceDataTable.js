    // 二级列表页面
    var dataTableSecondary;
    function initDataTableSecondary() {
        dataTableSecondary = $('.dataTableSecondary').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/secondary/data",
                "type": "post",
                "data": function (d) {
                     d.isDisabled = "1";
                     d.channelId = $('#channelId_Secondary').val();
                     d.name = $('#resourceName_Secondary').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "remarks", "bSortable": false}
            ],
            "columnDefs": []
        });

        dataTableSecondary.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#resourceName_Secondary").on('keyup', function () {
            dataTableSecondary.ajax.reload();
        });

    }
    // 图片
    var dataTableImg;
    function initDataTableImg() {
        dataTableImg = $('.dataTableImg').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/img/data",
                "type": "post",
                "data": function (d) {
                    d.isDisabled = "1";
                    d.channelId = $('#channelId_img').val();
                    d.name = $('#resourceName_img').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "picUrl", "bSortable": false}
            ],
            "columnDefs": [
                {
                    "render": function (data, type, row) {
                        var img='';
                        for(var i=0;i<data.length;i++){
                            img+='<img src="'+data[i]+'" alt="图片" height="30px" width="30px;">';
                        }
                        return img ;
                        // return '<a href=base+"/platform/scene/interface/pic?type=img&id=' + row.id + '" target="_blank" ><img src="'+data+'" alt="图片" height="30px" width="30px;"></a>';
                        //                      return '<div class="btn-group"><button type="button" class="btn btn-default btn-xs dropdown-toggle">' +
                        //                         '<a href=base+"/platform/scene/interface/pic?type=img&id=' + row.id + '" target="_blank" >查看</a>' +
                        //                          '</button></div>';

                    },
                    "targets":1
                }
            ]
        });

        dataTableImg.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#resourceName_img").on('keyup', function () {
            dataTableImg.ajax.reload();
        });

    }
    // 图片列表
    var dataTableImgCollection;
    function initDataTableImgCollection() {
        dataTableImgCollection = $('.dataTableImgCollection').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/imgCollection/data",
                "type": "post",
                "data": function (d) {
                    d.isDisabled = "1";
                    d.channelId = $('#channelId_imgList').val();
                    d.name = $('#resourceName_imgList').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "picUrl", "bSortable": false}
            ],
            "columnDefs": [
                {
                    "render": function (data, type, row) {
//                        var imgCollection='<a href=base+"/platform/scene/interface/pic?type=imgCollection&id=' + row.id + '" target="_blank" >';
//                        for(var i=0;i<data.length;i++){
//                            imgCollection+='<img src="'+data[i]+'" alt="图片" height="30px" width="30px;">';
//                        }
//                        return imgCollection+'</a>';
                        var imgCollection='';
                        for(var i=0;i<data.length;i++){
                            imgCollection+='<img src="'+data[i]+'" alt="图片" height="30px" width="30px;">';
                        }
                            return imgCollection;

                    },
                    "targets":1
                }
            ]
        });
        dataTableImgCollection.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });
         $("#resourceName_imgList").on('keyup', function () {
                    dataTableImgCollection.ajax.reload();
         });

    }
    // 自定义媒资
    var dataTableMedia;
    function initDataTableMedia() {
        dataTableMedia = $('.dataTableMedia').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/media/list/data",
                "type": "post",
                "data": function (d) {
                  d.levelId = 1;
                  d.publish = 1;
                  d.status = 1;
                  d.seriesName = $('#mediaName_search').val();
               }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "seriesName", "bSortable": true},
                {"data": "seriesCode", "bSortable": true},
                {"data": "cpName", "bSortable": true},
                {"data": "releaseYear", "bSortable": true},
                {"data": "showCharge", "bSortable": true}
            ]
        });
        dataTableMedia.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#mediaName_search").on('keyup', function () {
             dataTableMedia.ajax.reload();
        });

    }
    // html
    var dataTableArticle;
    function initDataTableArticle() {
        dataTableArticle = $('.dataTableArticle').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/html/data",
                "type": "post",
                "data":function (d) {
                    d.isDisabled = "1";
                    d.channelId = $('#channelId_html').val();
                    d.name = $('#resourceName_html').val();
                 }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "remarks", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableArticle.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#resourceName_html").on('keyup', function () {
            dataTableArticle.ajax.reload();
        });
    }
    // 视频
    var dataTableVideo;
    function initDataTableVideo() {
        dataTableVideo = $('.dataTableVideo').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/video/data",
                "type": "post",
                "data":function (d) {
                       d.isDisabled = "1";
                       d.channelId = $('#channelId_video').val();
                       d.name = $('#resourceName_video').val();
                 }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "remarks", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableVideo.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#resourceName_video").on('keyup', function () {
            dataTableVideo.ajax.reload();
        });
    }
    // 广告
    var dataTableAdv;
    function initDataTableAdv() {
        dataTableAdv = $('.dataTableAdv').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/advertise/data",
                "type": "post",
                "data":function (d) {
                   d.isDisabled = "1";
                   d.channelId = $('#channelId_adv').val();
                   d.name = $('#resourceName_adv').val();
              }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "fileType", "bSortable": false},
                {"data": "remarks", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableAdv.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });
         $("#resourceName_adv").on('keyup', function () {
             dataTableAdv.ajax.reload();
         });
    }
    // app
    var dataTableApp;
    function initDataTableApp() {
        dataTableApp = $('.dataTableApp').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/app/data",
                "type": "post",
                "data":function (d) {
                  d.isDisabled = "1";
                  d.channelId = $('#channelId_app').val();
                  d.name = $('#resourceName_app').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "packageName", "bSortable": false},
                {"data": "action", "bSortable": false},
                {"data": "remarks", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableApp.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });
        $("#resourceName_app").on('keyup', function () {
           dataTableApp.ajax.reload();
         });
    }
    // 内部网站
    var dataTableWebsite_internal;
    function initDataTableWebsite_internal() {
        dataTableWebsite_internal = $('.dataTableWebsite_internal').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/website/data",
                "type": "post",
                "data":function (d) {
                  d.isDisabled = "1";
                  d.channelId = $('#channelId_app').val();
                  d.type = "internal";
                  d.name = $('#resourceName_website_internal').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "url", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableWebsite_internal.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });
        $("#resourceName_website_internal").on('keyup', function () {
           dataTableWebsite_internal.ajax.reload();
         });
    }
    // 外部网站
    var dataTableWebsite_external;
    function initDataTableWebsite_external() {
        dataTableWebsite_external = $('.dataTableWebsite_external').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/cms/website/data",
                "type": "post",
                "data":function (d) {
                  d.isDisabled = "1";
                  d.channelId = $('#channelId_app').val();
                  d.type = "external";
                  d.name = $('#resourceName_website_external').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "name", "bSortable": false},
                {"data": "url", "bSortable": false}
            ],
            "columnDefs": []
        });
        dataTableWebsite_external.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });
        $("#resourceName_website_external").on('keyup', function () {
           dataTableWebsite_external.ajax.reload();
         });
    }
    // // vod网站
    // var dataTableWebsite_mediaSubject;
    // function initDataTableWebsite_mediaSubject() {
    //     dataTableWebsite_mediaSubject = $('.dataTableWebsite_mediaSubject').DataTable({
    //         "dom": '<"toolbar">frtip',
    //         "searching":false,
    //         "processing": false,
    //         "serverSide": true,
    //         "select": true,
    //         "ordering": true,
    //         "language": {
    //             "url": base+"/static/plugins/datatables/cn.json"
    //         },
    //         "preDrawCallback": function () {
    //             sublime.showLoadingbar($(".main-content"));
    //         },
    //         "drawCallback": function () {
    //             sublime.closeLoadingbar($(".main-content"));
    //         },
    //         "ajax": {
    //             "url": base+"/platform/cms/website/data",
    //             "type": "post",
    //             "data":function (d) {
    //               d.isDisabled = "1";
    //               d.channelId = $('#channelId_app').val();
    //               d.type = "mediaSubject";
    //               d.name = $('#resourceName_website_mediaSubject').val();
    //             }
    //         },
    //         "order": [[0, ""]],
    //         "columns": [
    //             {"data": "name", "bSortable": false},
    //             {"data": "url", "bSortable": false}
    //         ],
    //         "columnDefs": []
    //     });
    //     dataTableWebsite_mediaSubject.on('click', 'tr', function () {
    //         if(!$(this).hasClass('selected')){
    //             $(".dataTableWebsite_mediaSubject tbody tr").removeClass('selected');
    //         }
    //         $(this).toggleClass('selected');
    //     });
    //     $("#resourceName_website_mediaSubject").on('keyup', function () {
    //        dataTableWebsite_mediaSubject.ajax.reload();
    //      });
    // }
    // VOD专题
    var dataTableMediaSubject;
    function initDataTableMediaSubject() {
        dataTableMediaSubject = $('.dataTableMediaSubject').DataTable({
            "dom": '<"toolbar">frtip',
            "searching":false,
            "processing": false,
            "serverSide": true,
            "select": true,
            "ordering": true,
            "language": {
                "url": base+"/static/plugins/datatables/cn.json"
            },
            "preDrawCallback": function () {
                sublime.showLoadingbar($(".main-content"));
            },
            "drawCallback": function () {
                sublime.closeLoadingbar($(".main-content"));
            },
            "ajax": {
                "url": base+"/platform/media/subject/data",
                "type": "post",
                "data": function (d) {
                    d.subjectName = $('#subjectName_search').val();
                }
            },
            "order": [[0, ""]],
            "columns": [
                {"data": "subjectName", "bSortable": true},
                {"data": "remarks", "bSortable": true}
            ]
        });
        dataTableMediaSubject.on('click', 'tr', function () {
            if(!$(this).hasClass('selected')){
                $("tbody tr").removeClass('selected');
            }
            $(this).toggleClass('selected');
        });

        $("#subjectName_search").on('keyup', function () {
            dataTableMediaSubject.ajax.reload();
        });

    }

    $(document).ready(function () {
        initDataTableSecondary();
        initDataTableImg();
        initDataTableImgCollection();
        initDataTableMedia();
        initDataTableArticle();
        initDataTableVideo();
        initDataTableAdv();
        initDataTableApp();
        initDataTableWebsite_internal();
        initDataTableWebsite_external();
        //initDataTableWebsite_mediaSubject();
        initDataTableMediaSubject();
    });
