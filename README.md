#WAS Mission

##Step1

- 브라우저에서 오는 모든 요청은 input stream에 담겨있다.
- 서버에서 클라이언트에게 보내는 데이터는 outputStream에 담는다.
-  데이터를 라인별로 읽는게 힘들어서 Buffered Reader 로 읽는다
- byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath()) 형태로 response body 에 해당 경로의 파일 정보를 읽어서 클라이언트에게 전달한다.