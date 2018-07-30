package com.larry.webserver.mvc.viewFlow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;

public class ViewResolver {

    private final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public <E> byte[] resolve(String viewFileName, List<Model> models) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        log.info("model : {}", models);
        log.info("view file name is {}", viewFileName);
        if (models.isEmpty()) {
            return Files.readAllBytes(new File("webapp/" + viewFileName).toPath());
        }
        Model model = models.get(0);
        List<E> elems = model.getElems();
        List<String> html = Files.readAllLines(new File("webapp/" + viewFileName).toPath());
        int a = 0;
        String line = null;
        for (int i = 0; i < html.size()-2; i++) {
            if (html.get(i).contains("id=\""+model.getModelName()+"\"")) {
                log.info("found html line {}", html.get(i));
                html.remove(i);
                a = i;
                line = html.get(a);
                html.remove(a);
            }
        }
        html.addAll(a, genStrings(line, elems));
        for (String s : html) {
            System.out.println(s);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : html) {
            out.writeBytes(element);
        }
        return baos.toByteArray();
    }

    private <E> Collection<? extends String> genStrings(String s, List<E> elems) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> strings = new ArrayList<>();
        for (E e : elems) {
            log.info("바꿨다. {}", replaceOneLine(s, e));
            strings.add(replaceOneLine(s, e));
        }
        return strings;
    }

    private <E> String replaceOneLine(String line, E e) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> fields = getChangeFields(line);
        if (fields.size() == 0) {
            return line;
        }
        String replacedLine = replaceOneElem(line, fields.get(0), e);
        return replaceOneLine(replacedLine, e);
    }

    private List<String> getChangeFields(String line) {
        List<String> elems = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < line.length() - 3; i++) {
            if (line.substring(i, i + 2).startsWith("{{")) {
                start = i + 2;
            }
            if (line.substring(i, i + 2).startsWith("}}")) {
                end = i;
            }
            if (start != 0 && end != 0) {
                elems.add(line.substring(start, end));
                start = 0;
                end = 0;
            }
        }
        return elems;
    }

    private <E> String replaceOneElem(String line, String elem, E e) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return line.replace("{{" + elem + "}}", (CharSequence) e.getClass().getDeclaredMethod("get" + makeFirstletterUpperCase(elem)).invoke(e));
    }

    private String makeFirstletterUpperCase(String elem) {
        return elem.substring(0, 1).toUpperCase() + elem.substring(1);
    }

}
