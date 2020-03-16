# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 다음 단계를 도전하고 앞의 과정을 반복한다.
* 코드 리뷰가 완료되면 피드백에 대한 개선 작업을 한다.
* 다음 단계 PR 보낼 때 이전 단계 피드백이 잘 반영되었는지 점검한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://github.com/code-squad/codesquad-docs/blob/master/codereview/README.md)
* [동영상으로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://youtu.be/a5c9ku-_fok)

---

**Solar**

### Request Line에서 path 분리하기

- Reqeust Header의 시작줄은 Method, URL, HTTP버전 정보가 들어가 있다.

- 시작줄에서 요청 URL(위 예의 경우 /index.html 이다.)을 추출한다.

  - String[] tokens = line.split(" "); 활용해 문자열을 분리할 수 있다.

    ⇒ "GET", "/index.html", "HTTP/1.1"



### byte array to String

* `Files.readAllBytes`로 파일 내용을 읽어오면 byte 배열로 읽어온다.
* `new String(byte[], "인코딩종류")` 를 이용하면 String 생성자를 통해서 자동으로 String으로 만들어준다.

```java
byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
log.debug("body : {}", new String(body, "UTF-8"));
```



**Dion**

Request Header를 읽어올 때, InputStream을 Reader로 감싸서 `readLine()`을 이용하는 것이 좋다.

```java
BufferedReader br = new BufferedReader(new InputStreamReader(in));
```

Path에 해당하는 파일 읽어오는 방법`java.nio`의 `Files`를 이용해서 `Path`에 있는 파일을 읽어온다.

```java
byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
```