package util;

import model.RequestEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {
    @Test
    public void obtainReturnViewTest() {
        assertThat(ViewResolver.obtainReturnView(new RequestEntity("/index.html", "GET", null))).isEqualTo("./webapp/index.html");
    }
}
