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



---

**Solar**

### 동적인 html 생성

* 사용자목록을 tbody로 만든 후, list.html에서 사용자 목록 부분을 어떻게 대치할까 고민
* Spring 프로젝트를 진행했을 때 아이디어를 가져와서 list.html에 동적으로 받아와서 넣어줄 부분을 `{{users}}`라는 문자열을 넣어두고, 컨트롤러에서 `{{users}}`부분을 replace해서 넣어주도록 구현함



### stylesheet 파일 지원하도록 구현

* request header `Accept` 헤더 :  요청을 보낼 때 서버에 이런 타입(MIME)의 데이터를 보내줬으면 좋겠다고 명시할 때 사용

* css 파일을 요청할 때, `Accept: text/css`로 보낸다.
* response header에 `Content-Type: text/css` 를 추가해서 브라우저가 css 파일로 인식하도록 한다.
* 현재는 request url에 `.css`가 포함되어 있을 때, `Content-Type`을 css로 보내고 있는데, 추후에 request 헤더의 `Accept` 가 `text/css`일 경우에 보내주도록 수정하면 될 것 같다.

![image-20200318160149192](https://tva1.sinaimg.cn/large/00831rSTgy1gcy3cuwv6cj31jg0don12.jpg)



### 개선하고 싶은 점

* HttpRequest 객체가 `Request Line`에서 Method와 URL path 정보만 가지고 있는데, Http Request의 모든 정보(Request Header들과 Body)를 담도록 개선하고 싶다.
* HttpResponse 객체도 만들어서 중복을 제거하고 싶다.



**Dion**

### [Dion] STEP1 구현 후 정리

POST 요청을 보내면 파라미터는 Query String과 동일한 형태로 들어오고, Body에 담겨서 들어옵니다.

그래서 GET 요청의 Query String 처리 방식으로 POST 요청을 처리할 수 있습니다.

---
`302` 응답을 보낼 때, `Location` 헤더를 이용해서 `GET` 요청을 다시 보낼 경로를 지정해줄 수 있습니다.

---
### ISSUE: 세션에 로그인 유저 정보 저장하기
---
url을 비교할 때, `contains` 와 `equals` 를 혼용하니까, 곳곳에서 혼동이 생겨서 가급적이면 하나로 통일하는 편이 좋을 것 같습니다.

서버에서 Cookie를 설정하라는 응답을 보낼 때에는 `Set-Cookie` 헤더를 이용하면 쿠키를 저장하라고 할 수 있습니다.

Client에서 Cookie를 여러개 줄 경우 `name1=value1; name2=value2` 의 형식으로 보냅니다.

이는 `;`로 분리해서 `=`으로 key와 value를 구분해주면 됩니다.

---

`BufferedReader`를 생성할 때, `inputStream`을 받아서 생성을 하는데, 생성 후에는 inputStream이 비워져서 이를 생각하고 코딩을 해야 할 것 같습니다.

---

`String`에서 `byte[]`로 `byte[]`에서 `String`으로 변환하는 과정은 굉장히 매끄럽게 되는 것 같습니다.

`Response`를 줄 때, `Content-Type`을 정확히 줘야 Client가 읽어들일 수 있습니다.

