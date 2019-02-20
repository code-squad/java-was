package view;

import dao.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ViewResolver {

    private static final String ROOT_PATH = "./webapp/";

    public static byte[] resolve(String viewName, Model model) throws IOException {
        if (viewName.equals("redirect")) {
            return "".getBytes();
        }
        if (model.isEmptyModel()) {
            return Files.readAllBytes(new File(ROOT_PATH + viewName).toPath());
        }
        return viewWithModel(viewName, model);
    }

    private static byte[] viewWithModel(String viewName, Model model) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(ROOT_PATH + viewName));
        String line;

        while ((line = br.readLine()) != null) {
            if (isMustached(line)) {
                String key = extractAttributeName(line);
                StringBuilder convertingArticleBuilder = getConvertingArticleBuilder(br, key);
                line = convert(model.getAttribute(key), convertingArticleBuilder);
            }
            stringBuilder.append(line);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString().getBytes();
    }

    private static boolean isMustached(String line) {
        return line.contains("{{") && line.contains("}}");
    }

    private static String extractAttributeName(String line) {
        return line
                .replace("{{", "")
                .replace("}}", "")
                .replace(" ", "");
    }

    private static StringBuilder getConvertingArticleBuilder(BufferedReader br, String key)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        String iterativeLine = br.readLine();
        while (!iterativeLine.contains("{{/" + key + "}}")) {
            sb.append(iterativeLine);
            sb.append(System.lineSeparator());
            iterativeLine = br.readLine();
        }
        return sb;
    }

    private static String convert(List<List<String>> items, StringBuilder convertingArticleBuilder) {
        StringBuilder convertedChunk = new StringBuilder();
        for (List<String> item : items) {
            String convertingArticle = convertingArticleBuilder.toString();
            for (int i = 0; i < item.size(); i++) {
                convertingArticle = convertingArticle.replace("{{" + i + "}}", item.get(i));
            }
            convertedChunk.append(convertingArticle);
        }
        return convertedChunk.toString();
    }
}
