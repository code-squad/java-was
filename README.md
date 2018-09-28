# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://github.com/code-squad/codesquad-docs/blob/master/codereview/README.md)
* [동영상으로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://youtu.be/a5c9ku-_fok)

## 해당 과정을 진행하면서 알게된 것
* HTTP 요청 메세지의 구성
    1. request Line
    2. header for request
    3. body
* request line은 `Method SP Request-URI SP HTTP-Version CRLF`로 구성된다
* header는 `general-header`, `request header`, `entity header`로 구성된다.
    * 세 종류의 header 중 요청에 필요한 header만 사용한다.
* HTTP 응답 메세지의 구성
    1. status line
    2. header for response
    3. body
    

