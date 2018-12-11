package cn.phenix.model.ui.v1.enums;

/**
 * 场景动作
 *
 * @author xiaobin
 * @create 2017-10-11 下午1:16
 **/
public enum SceneAction {

    WINDOW {
        @Override
        public String actionName() {
            return "打开窗体";
        }

        @Override
        public String value() {
            return "WINDOW";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    },
    URL {
        @Override
        public String actionName() {
            return "打开链接";
        }

        @Override
        public String value() {
            return "URL";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    },
    APP {
        @Override
        public String actionName() {
            return "打开APP";
        }

        @Override
        public String value() {
            return "APP";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    },
    LAUNCHER {
        @Override
        public String actionName() {
            return "打开Launcher";
        }

        @Override
        public String value() {
            return "LAUNCHER";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    },
    HTML {
        @Override
        public String actionName() {
            return "渲染HTML";
        }

        @Override
        public String value() {
            return "HTML";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    }, CONTAINER {
        @Override
        public String actionName() {
            return "打开一个容器";
        }

        @Override
        public String value() {
            return "CONTAINER";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    },
    IAMGE {
        @Override
        public String actionName() {
            return "渲染image";
        }

        @Override
        public String value() {
            return "IAMGE";
        }

        @Override
        public String actionBefore() {
            return null;
        }

        @Override
        public String actionAfter() {
            return null;
        }
    };

    public abstract String actionName();

    public abstract String value();

    /**
     * 前置事件
     *
     * @return
     */
    public abstract String actionBefore();

    /**
     * 后置事件
     *
     * @return
     */
    public abstract String actionAfter();
}
