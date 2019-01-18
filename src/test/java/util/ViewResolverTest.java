package util;

import model.RequestHeader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {
    @Test
    public void obtainReturnViewTest() {
        assertThat(ViewResolver.obtainReturnView(new RequestHeader("/index.html", "GET", null))).isEqualTo("./webapp/index.html");
    }
}
