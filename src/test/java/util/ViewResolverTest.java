package util;

import model.URLInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {
    @Test
    public void obtainReturnViewTest() {
        assertThat(ViewResolver.obtainReturnView(new URLInfo("/index.html", "GET"))).isEqualTo("./webapp/index.html");
    }
}
