# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://github.com/code-squad/codesquad-docs/blob/master/codereview/README.md)
* [동영상으로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://youtu.be/a5c9ku-_fok)

## HTTP METHOD
|메서드|설명|메시지 본문이 있는가?|
|GET|서버에서 어떤 문서를 가져온다.|없음|
|HEAD|서버에서 어떤 문서에 대해 헤더만 가져온다.|없음|
|POST|서버가 처리해야 할 데이터를 보낸다.|있음|
|PUT|서버에 요청 메시지의 본문을 저장한다.|있음|
|TRACE|메시지가 프락시를 거쳐 서버에 도달하는 과정을 추적한다.|없음|
|OPTIONS|서버가 어떤 메서드를 수행할 수 있는지 확인한다.|없음|
|DELETE|서버에서 문서를 제거한다.|없음|