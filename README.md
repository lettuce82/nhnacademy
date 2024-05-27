# Spring MVC Final
## NHN Mart 고객센터
NHN Mart 소프트웨어를 통해서 성공적으로 매출이 발생하고 있다.
이 상황에서 고객의 불만을 접수하는 고객센터를 웹애플리케이션으로 제공하고자 한다.
아래 스펙을 보고 적절하게 구현해서 빠르게 문제를 해결해보자.
### 고객
- 고객의 계정정보는 이미 존재함.
  - 회원가입을 따로 만들 필요가 없단 소리다.
- 아이디, 비밀번호, 이름 등의 속성이 존재.
#### 로그인/로그아웃
- 로그인 후 고객 페이지로 접속
- 로그인 실패 시 예외
#### 내 문의 목록
- 고객 페이지에서는 내가 올린 문의 목록을 조회할 수 있음.
- 문의 글은 한 번 다 볼 수 있음(no-pagination)
- [문의하기] 버튼을 누르면 문의하기 할 수 있음
- 제목, 분류, 작성일시, 답변여부가 확인됨.
- 제목을 클릭하면 문의하기 상세 내역을 확인할 수 있음.
#### 문의하기 상세 내역
- 답변 안됨 경우 제목, 분류, 본문이 노출되고, 답변 안됨 마킹되어 있음
- 답변이 된 경우
  - 고객은 문의 내역에서 답변 내용, 답변 일시, CS담당자를 추가로 확인할 수 있다.
#### 문의하기
- 제목, 분류, 본문, 문의하기 작성일시, 작성자, 첨부파일 있음.
  - 분류: 불만 접수, 제안, 환불/교환, 칭찬해요, 기타 문의
- 첨부파일은 N개 올릴 수 있음. 첨부파일은 이미지(gif, jpg, jpeg, png)만 가능
- 본인이 올린 문의하기 글을 내가 다시 조회할 수 있음
- 문의하기가 성공하면 내가 문의한 내역들을 다시 조회됨
- 가장 최근에 문의한 글이 가장 상단에 위치
- 문의 분류 별로 [검색]하는 기능이 있음.
- [확인] ,[취소] 버튼이 있음
  - [확인] 버튼을 누를 때 제목, 본문이 비어 있으면 유효성 예외 발생함
    - 제목: 2~200자까지 가능(또는 byte로 해도 됨)
    - 본문: 0~40,000자까지 가능(또는 byte로 해도 됨)
  - [취소] 버튼을 누르면 메인(내 문의 목록) 페이지로 이동
### CS담당자
#### 로그인/로그아웃
#### 고객 문의 목록
- CS관리자 페이지로 로그인 후 답변이 달리지 않는 고객 문의에 답변을 달 수 있다.
- CS 관리자 페이지에서는 "답변이 달리지 않는 고객 문의 목록"만 볼 수 있다.
  - 한 번 답변이 달린 문의는 다시 답변을 달 수 없다.
#### 고객 문의 답변하기
- 하나의 문의를 선택 후 적절하게 답변한다.
  - 문의하기 상세 정보와 답변 text 영역이 노출된다.
  - 고객이 업로드한 이미지를 누르면 새창이나 팝업으로 확인할 수 있다. 또는 다운로드 되도 상관 없음.
  - 답변을 하면 고객은 문의 내역에서 답변 내용, 답변 일시, 답변 CS담당자를 추가로 확인할 수 있다.
- [답변하기] ,[취소] 버튼이 있음
  - [확인] 버튼을 누를 때 답변이 비어 있으면 유효성 예외 발생함
    - 답변: 1~40,000자까지 가능(또는 byte로 해도 됨)
  - [취소] 버튼을 누르면 메인(고객 문의 목록) 페이지로 이동
#### 개념 모델
![image](https://github.com/lettuce82/nhnacademy/assets/152861713/7032cd8b-8a4a-4fef-a93c-765ec057dea2)

### 이슈
##### issue 1 - LoginCheckInterceptor
HandlerInterceptor를 구현한 LoginCheckInterceptor 를 이용하여 session의 attribute에 userId가 있는지 확인하고(로그인 로직)
없다면 NoUserExistsException()(로그인 중인 유저가 없을 때 발생하는 예외)이 발생하도록 구현하였다.
다만, LoginCheckInterceptor 는 /cs/login이라는 Url(로그인 페이지)에서는 작동을 하면 안 되기 때문에
WebMvcConfigurer를 구현한 WebConfig에서 excludePathPatterns을 이용하여 해당 url은 제외되도록 구현하였는데,
다국어 처리할 때 들어가는 쿼리 스트링까지는 인식을 못 하여 언어 변경시 해당 예외 페이지로 리턴되었다.
-> 마지막까지 방법을 찾지 못 해서 로그인 페이지에서는 언어 변경 버튼을 지워서 제출했다.
##### issue 2 - DbConnectionAspect
JPA 사용 없이(JAP 배우기 전) JDBC를 이용하여 데이터베이스와 연동하여 구현하였다.
ThreadLocal를 이용하여 DB 연결을 관리했는데, dataSource와 connect, set 하는 작업을 initialize 메서드에서 처리하였고,
commint, rollback 하는 작업은 reset 메서드에서 구현하였다.
처음에는 이러한 initialize, reset를 repository의 메서드마다 기재했는데 이때 중복코드가 발생했다.

AOP를 이용하여 해결하였는데, DbConnectionAspect를 만들어 repository 하위의 메서드가 실행되기 전과 후를
@Before, @AfterReturning, @AfterThrowing를 이용하여 dbConnectionThreadLocal의 initialize, reset,
작업 과정에서 발생할 수 있는 예외 등을 리턴할 수 있도록 구현하였다.
##### issue 3 - 파일 업로드
프로젝트 내의 디렉토리에 저장하는 것과 File이라는 도메인 형태로 데이터베이스에 저장하는 것 두 가지를 구현하였다.
실제로 input태그를 통해서 받는 파일의 형태는 MultiPartFile이었으므로 FileConverter를 만들어 직접 구현하여 File 객체로 변형 후
fileRepository를 통하여 DB에 insert하였다.
디렉토리에 저장하는 것은 Paths의 get과 resolve를 통하여 경로를 지정한 후 Files의 write를 이용하여 구현하였다.
##### issue 4 - 파일 불러오기
파일이 업로드 되는 경로는 src/main/resources/static 외부에 있는 곳으로 설정했다.
정적 리소스 파일이 src/main/resources/static 디렉토리 밖에 있기 때문에 Spring MVC에서는 자동으로 이를 인식하지 못했다.
따라서 html에서 업로드한 이미지 파일을 읽어오기 위해 img 태그의 src 속성에 경로를 지정하여도 불러오지 못했다.

addResourceHandlers를 이용하여 해결하였다. th:src="@{/uploads/{filename}(filename=${file.name})}" 의 표현으로
동적인 URL을 생성하고 이러한 URL 패턴의 경우 addResourceHandlers의 addResourceLocations로 실제 파일 경로를 지정하여
파일 경로를 인식하여 불러올 수 있도록 처리하였다.
