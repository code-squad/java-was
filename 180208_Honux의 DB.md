## Honux의 DB

RDB 는 기본적으로 Row store.

Column store 는 분석에 유리하다. ( = sum() 같은거 할때..)

Super block => meta id 를 가지고 있는녀석

물리 공간에 row 를 저장해나감. Offset 을 페이지 저장공간 끝부터 써나가서 빠른 접근을 가능하게함

BST =>  AVL, RB => (디스크버전) B+ Tree : 아주 적은 IO 로도 

PK는 B+Tree 형태로 저장이됨. 그래서 빠른 접근이 가능하다. =>. Clustered Index. (index가 아니라 db의 저장방식.)

Name 과 같이 자주 쓰는 컬럼들은 index를 만든다. CREATE INDEX ON EMP(NAME)
=> 얘는 PK 를 가지고 있음.(이렇게 하면 업데이트가 느리다)

빠른 검색을 위해 두 Column 을 결합한 복합키를 만든다(?)

FK 는 참조하는 테이블의 값이거나 Null이어야한다 : 참조무결성

Nasted Loop Join => OLTP에선 사실상 이것만 쓸 수 있음/. MySQL에선 이것밖에 없음.

Hash Join => 거의 N으로 빠른데, CPU 연산을 많이 쓰게됨. OLTP 에선 쓸수가 없음

Sort Merge Join => 레코드가 정렬된 상태에서 쓰는겅뮈. 빠름.

OLTP => 전자상거래 처럼 많은 사람들이 작은 쿼리를 날리는 서비스