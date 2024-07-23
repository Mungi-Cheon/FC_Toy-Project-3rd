# Toy Project 3 FastCampus BE 8기 3조

## 🛣️ Spring 기반 여행 & 여정 관리 애플리케이션

---

## 프로젝트 소개 및 목표

---

### 프로젝트 소개

- 여행, 여정을 관리하는 SNS 서비스 API입니다.
- `Spring` 기반으로 개발되었습니다.

### 프로젝트 목표

- DDD 패키지 구성
- DB 설계
- Spring Security와 JWT의 이해 및 활용
- OpenApi의 활용

위의 사항을 지향하였습니다.

---

## 팀원 구성

|                                                                **안지수**                                                                 |                                                                **김태민**                                                                 |                                                                **하정훈**                                                                 |                                                                **천문기**                                                                 |
|:--------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/Mungi-Cheon/HACK_MartService/assets/159132478/80cc52ee-80d6-4be6-9caa-dcca017a58fc" height=150 width=250> | <img src="https://github.com/Mungi-Cheon/HACK_MartService/assets/159132478/2aa5b281-235b-4784-8ddc-732df3cc9ba9" height=150 width=250> | <img src="https://github.com/Mungi-Cheon/HACK_MartService/assets/159132478/504c818a-19c8-44f0-94a2-b428596ab5c4" height=150 width=250> | <img src="https://github.com/Mungi-Cheon/HACK_MartService/assets/159132478/eff6bcf3-2bc8-4a57-a6f8-e8017cd170e9" height=150 width=250> |

## 개발 기간

- 2024-5-27 ~ 2024-6-02

---

## 개발 환경

- Language :
    - Open JDK 17 (Corretto)
- Spring Framework Version :
    - v6.1.6
- Spring Boot Version :
    - v3.2.5
- Build :
    - Gradle v8.7
- DB & ORM :
    - MySQL, JPA
- Testing :
    - Postman
    - junit
    - mockito
- Dependency :
    - Spring Security : v3.2.5
    - jwt : 0.12.3
    - Lombok : v1.18.32
    - MySql Driver : v8.3.0
    - Spring Doc Open API : v2.2.0
- OpenApi :
    - 카카오 지도 Open API
- Collaboration Tools :
    - Git
    - GitHub
    - Jira
    - Discord
- 코드 컨벤션
    - 구글 컨벤션
- 커밋 컨벤션
    ```text
    FEAT : 새로운 기능의 추가
    FIX: 버그 수정
    DOCS: 문서 수정
    STYLE: 스타일 관련 기능(코드 포맷팅, 세미콜론 누락, 코드 자체의 변경이 없는 경우)
    REFACTOR: 코드 리펙토링
    TEST: 테스트 코트, 리펙토링 테스트 코드 추가
    CHORE: 빌드 업무 수정, 패키지 매니저 수정(ex .gitignore 수정 같은 경우) ex) feat/ A기능
    ・Commit 내용엔 최대한 간결하게 표현하기
    예시 ) FEAT : A기능 추가(or 수정)
  ```

---

## E-R Diagram

![ERD.png](src/main/resources/images/ERD.png)

---

## Application 실행

- 사전에 카카오의 REST API KEY를 발급받습니다.
- 임의의 로컬 경로에 프로젝트를 클론합니다.
- [SQL 파일](src/main/resources/sql) 에 있는 sql 파일들을 실행합니다.
- src/main/resources 경로에 application.yml 파일 생성합니다.
- 아래 내용을 application.yml에 붙여넣습니다.
- url, username, password를 입력합니다.
- 발급받은 카카오 REST API KEY를 붙여넣습니다.
- 애플리케이션을 실행해 주시면 됩니다.
- Swagger UI은 다음 URL로 접속해주세요. [SwaggerUI](http://localhost:8080/swagger-ui/index.html)

```yaml
spring:
  jwt:
    secret: superSecretKey12345!@#$%superSecretKey12345!@#$%
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: JDBC_URL/toy3travel
    username: 유저 이름
    password: 패스워드
springdoc:
  packages-to-scan: org.hack.travel.domain
  default-consumes-media-type: application/json;charset=UTF-8
  default-produces-media-type: application/json;charset=UTF-8
  swagger-ui:
    path: /
    disable-swagger-default-url: true
    display-request-duration: true
    operations-sorter: alpha
server:
  port: 8080
kakao:
  rest:
    api-key: 카카오 REST API KEY
```

---

## 기능 구현

- 유저 도메인 개발
    - 회원가입, 로그인, 로그아웃 기능 개발
    - Spring Security, JWT를 통해 access token, refresh token을 발행해 인증/인가 기능 구현
- 키워드가 포함된 관련 여행 정보를 조회하는 기능 개발
- 카카오 지도 Open API를 사용해 여정 정보에 대한 위치 정보를 생성
- 여행 정보에 대한 좋아요, 댓글관련 기능 개발

### 회원 가입

![회원 가입.png](src/main/resources/images/signUp.png)

---

### 로그인

![로그인.png](src/main/resources/images/login.png)

![토큰 발행.png](src/main/resources/images/publishToken.png)

---

### 로그아웃

![로그아웃.png](src/main/resources/images/logout.png)

---

### 전체 여행 조회

![전체 여행 조회.png](src/main/resources/images/findAllTrip.png)

---

### 여행 조회 (키워드)

![여행 조회_키워드.png](src/main/resources/images/tripSearchByKeyword.png)

---

### 여정 등록

![여정 등록 요청.png](src/main/resources/images/itineraryCreateReq.png)
![여정 등록 결과.png](src/main/resources/images/itineraryCreateRes.png)

---

### 여정 수정

![여정 수정 요청.png](src/main/resources/images/itineraryUpdateReq.png)
![여정 수정 결과.png](src/main/resources/images/itineraryUpdateRes.png)

---

### 댓글 생성

![댓글 생성.png](src/main/resources/images/createComments.png)

---

### 댓글 조회

![댓글 조회.png](src/main/resources/images/findComments.png)

---

### 댓글 수정

![댓글 수정.png](src/main/resources/images/updateComments.png)

---

### 댓글 삭제

![댓글 삭제.png](src/main/resources/images/deleteComments.png)

---

### 좋아요 생성

![좋아요 생성.png](src/main/resources/images/createLike.png)

---

### 여행 정보의 좋아요 개수 조회

![여행정보 좋아요 개수 조회.png](src/main/resources/images/findAllTripsAndLikeCnt.png)

---

### 좋아요 누른 여행 정보 조회

![좋아요 누른 여행 정보.png](src/main/resources/images/likedTrips.png)

---

### 좋아요 취소

![좋아요 취소.png](src/main/resources/images/cancelLike.png)

---

## 패키지 구조

![package.png](src/main/resources/images/package.png)

---





