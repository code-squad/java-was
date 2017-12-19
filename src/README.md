최종 학습한 내용

http
-------
response
request
-------

먼저 클라이언트가 웹서버에 접근
(접근 방법1 url을 이용한 접근)(get)
(http method를 이용한 접근)(get 이외)
request 요청
request는 header와 body로 구성됨

post의 값이 body에 들어가 있음. 이외에 delete 등 get을 제외한 다른
method들도 body에 값이 있음.

request 요청을 받고 그에 맞게 response를 만들어 클라이언트에게 보냄
response 또한 header와 body로 나누어져 있고
header에는 http의 status code와 http 버전정보를 담고 있고
그 외에 cookie정보 등을 담을 수 있음
추가로 뭘 더 담을 수 있는지 확인 필요
