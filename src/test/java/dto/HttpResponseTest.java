package dto;

import org.junit.Test;
import util.HttpCode;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseTest {
    @Test
    public void forward() throws Exception {
        HttpResponse res = new HttpResponse(HttpCode.OK);
        res.forward("index.html");
        assertThat(res.toString()).contains("INDEX.HTML");
    }
}
