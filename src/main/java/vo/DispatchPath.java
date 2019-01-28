package vo;

import controller.*;

public enum DispatchPath {
    CSS_PATH("/css", new StyleSheetController()),
    JS_PATH("/js", new JavaScriptController()),
    FONTS_PATH("/fonts", new FontsController()),
    INDEX_PATH("/index.html", new IndexController()),
    LOGIN_PATH("/user/login", new LoginController()),
    USER_LIST_PATH("/user/list", new ListUserController()),
    USER_PATH("/user", new CreateUserController());

    private String path;
    private Controller controller;

    DispatchPath(String path, Controller controller) {
        this.path = path;
        this.controller = controller;
    }

    public static Controller findController(String path) {
        for (DispatchPath dispatchPath : DispatchPath.values()) {
            if (path.startsWith(dispatchPath.path))
                return dispatchPath.controller;
        }
        return USER_PATH.controller;
    }

}
