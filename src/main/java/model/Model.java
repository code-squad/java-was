package model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private static Map<String, ClientModel> models = new HashMap<>();

    private String jSessionId;

    public Model() {

    }

    public Model(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public static ClientModel obtainModel(String jSessionId) {
        return models.get(jSessionId);
    }

    public void addAttribute(String key, Object object) {
        if(models.containsKey(this.jSessionId)) {
            models.put(this.jSessionId, this.models.get(jSessionId).addAttribute(key, object));
        }
        models.put(this.jSessionId, new ClientModel().addAttribute(key, object));
    }

    public static boolean isModel(String jSessionId) {
        return models.containsKey(jSessionId);
    }
}
