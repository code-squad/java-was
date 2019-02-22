package util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLineUtilsTest {

    @Test
    public void getUrl_success() {
        String input = "GET /index.html HTTP/1.1";
        String result = RequestLineUtils.getUrl(input);
        assertThat(result).isEqualTo("/index.html");
    }

    @Test
    public void getUrl_fail() {
        String input = "GET /index.html? HTTP/1.1";
        String result = RequestLineUtils.getUrl(input);
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void getUrl_회원가입_success() {
        String input = "GET /user/create?userId=11&password=1122&name=33&email=1%401 HTTP/1.1";
        String result = RequestLineUtils.getUrl(input);

    }

    @Test
    public void HttpMethod_가져오기_GET() {
        String input = "GET /index.html HTTP/1.1";
        String result = RequestLineUtils.getHttpMethod(input);
        assertThat(result).isEqualTo("GET");
    }

    @Test
    public void HttpMethod_가져오기_POST() {
        String input = "POST /user/create HTTP/1.1";
        String result = RequestLineUtils.getHttpMethod(input);
        assertThat(result).isEqualTo("POST");
    }


}