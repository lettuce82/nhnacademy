# Spring MVC Day 1
### 요구사항
학생 정보를 등록, 조회, 수정할 수 있는 Controller를 구현하시오.
#### 프로젝트 구성
- maven project
- pacaging: jar
- JDK 21, JAVA 21
- Spring Boot 3.2.5
#### 클래스 작성
기본적으로 아래 3개의 클래스를 구현해야하고 필요시 클래스를 추가로 생서해도 무방하다.
과제 내용과 관련없는 코드가 포함되어 있으면 안 된다.
- StudentLoginController
- StudentRegisterController
- StudentRepositoryImpl
#### Model 관련
- @ModelAttribute의 두가지 사용 방법ㅇ르 모두 써야한다.
- Model, ModelMap, ModelAndView를 각각 최소 1번 이상 사용해야 한다.
#### GET/student/{studentId}?hideScore=yes
- 이 경우에는 점수와 평가 항목을 출력하지 않는다.
- GET/student/{studentId} 와는 다른 별도의 Controller Method로 작성한다.
#### 쿠키를 이용한 로그인 기능
- SESSION 이라는 이름을 가진 쿠키가 있으면 로그인 된 것으로 간주한다.
- 로그인하지 않고 다른 기능은 사용할 수 없다.
- GET /login 경로로 접근하면 SESSION 쿠키를 확인하고
  - 있으면 학생 정보 조회 화면(studentView)을 보여준다.
  - 없으면 로긍니 폼(loginForm)을 보여준다.
- POST /login 경로로 접근하면 입력받은 id, password와 일치하는 학생이 있는지 확인하고
  - 있으면 SESSION 쿠키를 만들고 학생 정보 조회 화면(studentView)을 보여준다.
  - 없으면 로그인 폼(loginForm)으로 redirect 한다.
#### 화면구성
##### 로그인 화면
- 비밀번호는 마스킹 처리한다.
![image](https://github.com/lettuce82/nhnacademy/assets/152861713/ee5a217a-ff7a-46d7-8dbc-7c23b658c1ae)
##### 학생 정보 조회 화면(studentView.html)
- 비밀번호는 마스킹 처리해서 보여준다.
![image](https://github.com/lettuce82/nhnacademy/assets/152861713/bf8ea368-c326-45d1-b081-ba82c4f8d73e)
##### 학생 정보 등록 화면(studentRegister.html)
- 등록 후엔 등록된 학생의 학생 정보 등록 화면을 보여준다.
![image](https://github.com/lettuce82/nhnacademy/assets/152861713/0e968121-461f-43f3-a469-9f7a7362b151)
##### 학생 정보 수정 화면(studentModify.html)
- 학생 정보 입력 화면과 동일하나 각 항목별 수정 전 내용이 표시되어야 한다.
- 수정 후엔 수정된 학생의 학생 정보 등록 화면을 보여준다.

### 이슈
##### issue 1
생성했던 추가적인 클래스로는 따로 구현한 Exception 클래스 세개였다.
Exception이 발생하는 경우 다른 설정 없이도 error.html로 이동했다.
Exception 발생시 함께 전송하는 message를 html에 보내어 출력하는 것을 구현하기로 했다.

##### 경과
기본적으로 Spring Boot는 ErrorController를 사용하여 예외를 처리하며,
이 컨트롤러는 /error 경로에 매핑된다. 그리고 예외가 발생하면 이 경로에 대한 요청이 자동으로 생성된다.
ErrorController를 구현하여 사용자정의의 ErrorController를 만들 수 있는데,
이를 통해 경로 수정은 성공했지만 Exception의 Message를 가져오지 못 했다.
-> Day 2 수업에서 처리 방법을 배워 Day 2 과제에서 예외처리를 해냈다!

##### issue 2
처음에는 @ModelAttribute("student") 를 통하여 자동으로 @PathVariable("id") 값으로 student를 리턴했다.
그러나 이후 학생 등록 로직에서 Get으로 새로운 id 값을 보내기 때문에 등록된 학생이 없다는 에러가 발생했다.

##### 경과
@PathVariable("id) 값이 아닌 로그인시에 SESSION에 담았던 id 값으로 student를 리턴하도록 수정했다.
