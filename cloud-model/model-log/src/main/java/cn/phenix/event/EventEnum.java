package cn.phenix.event;

/**
 * 事件枚举
 *
 * @author xiaobin
 * @create 2017-08-27 上午10:03
 **/
public enum EventEnum {

    NULL {
        @Override
        public String getCode() {
            return "NULL";
        }

        @Override
        public String getInfo() {
            return "";
        }
    },
    DEVICE_LOGIN_EVENT {
        @Override
        public String getCode() {
            return "DEVICE_LOGIN_EVENT";
        }

        @Override
        public String getInfo() {
            return "设备登录事件";
        }
    },
    DEVICE_REFRESH_EVENT {
        @Override
        public String getCode() {
            return "DEVICE_REFRESH_EVENT";
        }

        @Override
        public String getInfo() {
            return "设备刷新事件";
        }
    },
    DEVICE_UPDATE_EVENT {
        @Override
        public String getCode() {
            return "DEVICE_UPDATE_EVENT";
        }

        @Override
        public String getInfo() {
            return "设备更新事件";
        }
    },
    MEDIA_CHECK_AUTH {
        @Override
        public String getCode() {
            return "MEDIA_CHECK_AUTH";
        }

        @Override
        public String getInfo() {
            return "媒资鉴权事件";
        }
    },
    MEDIA_CATEGORY_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_CATEGORY_EVENT";
        }

        @Override
        public String getInfo() {
            return "预览媒资分类事件";
        }
    },
    MEDIA_LIST_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_LIST_EVENT";
        }

        @Override
        public String getInfo() {
            return "预览媒资列表事件";
        }
    },
    MEDIA_DETAIL_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_DETAIL_EVENT";
        }

        @Override
        public String getInfo() {
            return "预览媒资详情事件";
        }
    }, ORDER_LAUNCH_EVENT {
        @Override
        public String getCode() {
            return "ORDER_LAUNCH_EVENT";
        }

        @Override
        public String getInfo() {
            return "发起订单事件";
        }
    }, ORDER_QR_PAY_EVENT {
        @Override
        public String getCode() {
            return "ORDER_QR_PAY_EVENT";
        }

        @Override
        public String getInfo() {
            return "发起支付事件（二维码）";
        }
    }, ORDER_BACK_EVENT {
        @Override
        public String getCode() {
            return "ORDER_BACK_EVENT";
        }

        @Override
        public String getInfo() {
            return "订单回调事件";
        }
    }, MEDIA_START_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_START_EVENT";
        }

        @Override
        public String getInfo() {
            return "播放";
        }
    }, MEDIA_PAUSE_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_PAUSE_EVENT";
        }

        @Override
        public String getInfo() {
            return "暂停";
        }
    }, MEDIA_BUFFER_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_BUFFER_EVENT";
        }

        @Override
        public String getInfo() {
            return "缓冲";
        }
    }, MEDIA_END_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_END_EVENT";
        }

        @Override
        public String getInfo() {
            return "播放结束";
        }
    }, MEDIA_EXIT_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_EXIT_EVENT";
        }

        @Override
        public String getInfo() {
            return "播放退出";
        }
    }, MEDIA_SEEK_EVENT {
        @Override
        public String getCode() {
            return "MEDIA_SEEK_EVENT";
        }

        @Override
        public String getInfo() {
            return "快进/快退";
        }
    };

    public abstract String getCode();

    public abstract String getInfo();
}
