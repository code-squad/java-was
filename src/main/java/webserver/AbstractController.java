package webserver;

abstract public class AbstractController implements Controller1{

    abstract public void service(HttpRequest request, HttpResponse response);
//
//    public void doGet(HttpRequest request, HttpResponse response){
//
//    }
//    public void doPost(HttpRequest request, HttpResponse response);
}
