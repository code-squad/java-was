package util;

import dto.HttpRequest;
import fixture.PostQuery;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpParserTest {
    @Test
    public void parseTest() throws Exception{
        HttpRequest httpRequest = HttpParser.parse(PostQuery.get());

        assertThat(httpRequest.method()).isEqualTo("POST");
        assertThat(httpRequest.url()).isEqualTo("/user/create");
        assertThat(httpRequest.version()).isEqualTo("HTTP/1.1");
        assertThat(httpRequest.header("Origin")).isEqualTo("http://localhost:8080");
        assertThat(httpRequest.header("User-Agent")).isEqualTo("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        assertThat(httpRequest.header("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.query("userId")).isEqualTo("id");
        assertThat(httpRequest.query("password")).isEqualTo("password");
    }
}
