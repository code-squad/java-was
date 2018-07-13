# 웹 애플리케이션 서버
## 7.11

* `File`, `Path`

* `Files.readAllBytes`

* `while (!"".equals(line)) {}`

* `InputStream`, `InputStreamReader`, `BufferedReader`

*  `OutputStream`, `DataOutputStream`

* ```java
      public static String readData(BufferedReader br, int contentLength) throws IOException {
          char[] body = new char[contentLength];
          br.read(body, 0, contentLength);
          return String.copyValueOf(body); // new String(body)?
      }
  ```

  ---

* 입출력 스트림에 대해서 공부해야 한다.
* 스레드에 대해서 공부해야 한다.
* 