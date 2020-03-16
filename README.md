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

-------------

##Ground Rule
* 컴퓨터 사용은 50분 단위로 교체하기로 한다.
    * 작업한 것을 커밋한다. (master)
* 최소 각자 2번은 진행한다.
* 화 : 13시-17시
* 수 : 12시-14시
* daily scrum 
    * 끝나고 1시간 진행하기로 한다.

##Repository 관리
* 하나의 repo를 공유한다.
    * https://github.com/Hamill210/java-was
* 해야할 일들을 issue에 기록한다.
* branch는 3개가 있습니다 : Hamill, Dan, Master
    * 각자의 branch로 작업을 하고 작업한 것을 master에 push한다.
    * 다음 차례 사람은 master branch를 pull한다.

## 커밋 메세지 가이드
### Commit Type
* feat : 새로운 기능 추가
* refactor : 코드 리팩토링

### 꼬리말 footer
* associate with : #이슈번호

```aidl
feat : index page 구현

- 페이지 호출
    - /resource/index.html 호출

associate with : #1
```
