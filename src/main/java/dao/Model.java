package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private Map<String, List<List<String>>> models = new HashMap<>();

    public void setAttribute(String key, List<List<String>> values) {
        models.put(key, values);
    }

    public List<List<String>> getAttribute(String key) {
        return models.get(key);
    }

    public boolean isEmptyModel() {
        return models.isEmpty();
    }
}
