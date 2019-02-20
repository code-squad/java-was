package view;

import dao.Model;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ViewResolverTest {

    @Test
    public void viewResolveTEst() throws Exception {
        byte[] ret = ViewResolver.resolve("./view_test.html", makeModel());
        System.out.println(new String(ret));
    }

    private Model makeModel() {
        Model model = new Model();
        List<List<String>> lists = new ArrayList<>();

        List<String> values = new ArrayList<>();
        values.add("0번");
        values.add("1번");
        values.add("2번");
        values.add("3번");

        lists.add(values);
        lists.add(values);

        model.setAttribute("user", lists);

        lists = new ArrayList<>();
        values = new ArrayList<>();
        values.add("제목 1");
        values.add("제목 2");
        lists.add(values);

        values = new ArrayList<>();
        values.add("title 1");
        values.add("contents 2");
        lists.add(values);

        model.setAttribute("table", lists);
        return model;
    }
}
