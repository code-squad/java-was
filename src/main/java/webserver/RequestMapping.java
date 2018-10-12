package webserver;

import controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private final static RequestMapping requestmapping = new RequestMapping();
    private Map<String, Controller> mappingTable = new HashMap<>();

    private RequestMapping() {
        initMappingTable();
    }

    public static RequestMapping of() {
        return requestmapping;
    }

    private void initMappingTable() {
        mappingTable.put("/user/create", new CreateUserController());
        mappingTable.put("/user/login", new LoginController());
        mappingTable.put("/user/list", new ListUserController());
        mappingTable.put("/user/form.html", new ResourceController());
        mappingTable.put("/user/login.html", new ResourceController());
        mappingTable.put("/user/login_failed.html", new ResourceController());
        mappingTable.put("/css/styles.css", new ResourceController());
        mappingTable.put("/css/bootstrap.min.css", new ResourceController());
        mappingTable.put("/index.html", new ResourceController());
    }

    public Controller get(String path) {
        return mappingTable.get(path);
    }
}
