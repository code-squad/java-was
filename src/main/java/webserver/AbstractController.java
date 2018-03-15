package webserver;

public abstract class AbstractController implements Controller {
    // 현재 추상 메소드가 존재하지 않지만 이 클래스의 인스턴스의 생성을 방지하기 위해서 추상 클래스로 설정했다.

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();
        if(method.isGet()) doGet(request, response);
        if(method.isPost()) doPost(request, response);
    }

    public void doPost(HttpRequest request, HttpResponse response) {
    }


    public void doGet(HttpRequest request, HttpResponse response) {
    }

}
