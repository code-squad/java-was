# WAS Mission

## Step1

- 브라우저에서 오는 모든 요청은 input stream에 담겨있다.
- 서버에서 클라이언트에게 보내는 데이터는 outputStream에 담는다.
-  데이터를 라인별로 읽는게 힘들어서 Buffered Reader 로 읽는다
- byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath()) 형태로 response body 에 해당 경로의 파일 정보를 읽어서 클라이언트에게 전달한다.



##  Step2

- 프레임워크에서 자동으로 query 를 parsing 해주는건 정말 꿀같은 거였다...



##  Step3

- POST 메소드에서는 파라미터가 header 하단 공백 다음에 온다.



## Step4

- header에 Location 을 추가해서 보내줌으로써 post 메소드의 redirectURL 을 설정할 수 있다.



## Step5

- Set-Cookie 를 통해 유저의 로그인 상태를 확인할 수 있다.
- Set-Cooke 를 설정해서 클라이언트에게 응답을 요청하면, 클라이언트는 다시 cookie 에 로그인 상태를 담아서 request를 보낸다.



## Step6

- Handlebars가 그립다.. 템플릿 적용하는 것이 어렵다.



## Step7

- 서버 - 브라우져의 통신에서 Content-Type 을 설정해주지 않으면(또는 잘못설정하면) 브라우져는 인식을 하지 못한다.



