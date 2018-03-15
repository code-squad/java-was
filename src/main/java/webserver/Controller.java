package webserver;

public interface Controller {
    // 각 요청과 응답에 대한 처리를 담당하는 부분을 추상화
    void service(HttpRequest request, HttpResponse response);

}
