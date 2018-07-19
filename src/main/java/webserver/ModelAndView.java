package webserver;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;

public class ModelAndView {

    private Map<String, Object> model = new HashMap<>();
    private String view;

    public ModelAndView(String view) {
        this.view = view;
    }

    public void setAttribute(String key, Object value) {
        model.put(key, value);
    }

    public boolean isEmpty() {
        return model.isEmpty();
    }

    public Object getAttribute(String key) {
        return model.get(key);
    }

    public byte[] createView() throws Exception{
        if (isEmpty()){
            return Files.readAllBytes(new File("./webapp" + view).toPath());
        }
        return transfer().getBytes();
    }

    public String transfer() throws Exception {
        byte[] body = Files.readAllBytes(new File("./webapp" + view).toPath());
        String beforeBody = new String(body);
        List<String> keys = getKey();
        return replaceHtml(beforeBody, keys);
    }

    public List<String> getKey() {
        Set<String> keySet = model.keySet();
        Iterator<String> keySets = keySet.iterator();
        List<String> keys = new ArrayList<>();
        while (keySets.hasNext()) {
            keys.add(keySets.next());
        }
        return keys;
    }

    public String replaceHtml(String origin, List<String> keys) throws Exception {
        String target = "";
        String keyStart = "";
        String keyFinsh = "";
        StringBuilder result = new StringBuilder();

        for (String key : keys) {
            Object value = getAttribute(key);
            keyStart = keyStart(key);
            keyFinsh = keyFinsh(key);
            if (origin.contains(keyStart) && origin.contains(keyFinsh)) {
                result = new StringBuilder();
                result.append(saveBefore(origin, keyStart));
                target = parseTarget(origin, keyStart, keyFinsh);
                result = checkCollection(result, value, target);
                result.append(saveAfter(origin, keyStart, keyFinsh));
                origin = result.toString();
            }
        }
        return result.toString();
    }

    public String parseTarget(String origin, String keyStart, String keyFinsh) {
        String target = origin.substring(origin.indexOf(keyStart), origin.indexOf(keyFinsh) + keyFinsh.length());
        target = target.replace(keyStart, "");
        return target.replace(keyFinsh, "");
    }

    public StringBuilder checkCollection(StringBuilder result, Object value, String target) throws Exception {
        if (!isCollection(value)) {
            return result.append(changeValue(target, value));
        }
        Collection<?> values = (Collection<?>) value;
        Iterator<?> iterator = values.iterator();
        while (iterator.hasNext()) {
            result.append(changeValue(target, iterator.next()));
        }
        return result;
    }

    public String changeValue(String orginal, Object value) throws Exception {
        String param = "";
        String result = "";
        while (orginal.contains("{{")) {
            param = orginal.substring(orginal.indexOf("{{") + 2, orginal.indexOf("}}"));
            result = invoke(param, value);
            orginal = orginal.replace("{{" + param + "}}", result);
        }
        return orginal;
    }

    public String saveBefore(String origin, String keyStart) {
        return origin.substring(0, origin.indexOf(keyStart));
    }

    public String saveAfter(String origin, String keyStart, String keyFinsh) {
        return origin.substring(origin.indexOf(keyFinsh) + keyFinsh.length());
    }

    public String invoke(String taget, Object value) throws Exception {
        Class valueClass = value.getClass();
        char[] parameter = taget.toCharArray();
        parameter[0] = Character.toUpperCase(parameter[0]);
        String result = new String(parameter);
        Method method = valueClass.getMethod("get" + result);
        return method.invoke(value).toString();
    }

    public String keyStart(String key) {
        return String.format("{{#%s}}", key);
    }

    public String keyFinsh(String key) {
        return String.format("{{/%s}}", key);
    }

    public boolean isCollection(Object object) {
        return object instanceof Collection<?>;
    }
}
