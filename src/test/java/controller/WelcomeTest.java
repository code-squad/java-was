package controller;

import Controller.WelcomeController;
import org.junit.Test;

public class WelcomeTest {

  WelcomeController wc = new WelcomeController();

  @Test
  public void doWork() {
    String requestLine =
        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
    String[] tokens = requestLine.split(" ");

    wc.doWork(tokens);
  }
}
