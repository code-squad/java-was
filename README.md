# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://github.com/code-squad/codesquad-docs/blob/master/codereview/README.md)
* [동영상으로 살펴보는 코드스쿼드의 온라인 코드 리뷰 과정](https://youtu.be/a5c9ku-_fok)


## 요구사항 1단계 잠깐휴식
* InputStream은 바이트의 흐름을 받아들이거나 내보내는 객체. (01010001 ....)
바이트기 때문에 바로 문자로 바꾸는 등의 처리가 힘들어서 Reader라는 객체를 사용한다.
Reader객체는 바이너리 스트림을 문자로 바꿔서 입출력 처리를 할 수 있게 해준다.
Reader객체는 한 문자단위로 입출력을 한다.

-> 그래서 Buffer를 사용한다.
BufferReader는 readLine()이라는 한줄씩 읽을수있는 메서드가 있기때문에 한줄을 통째로 String으로 저장할수있다.


## 요구사항 3단계 잠깐휴식
redirect 방식은 내부적으로 HTTP Status 302코드와 Location을 활용한다.

