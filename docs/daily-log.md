# 📘 Daily Log

> 프로젝트 진행 과정을 Day별로 기록합니다.  
> 문제 해결 과정과 배운 점도 함께 정리합니다.

---

## Day 1 (2025-09-21)

### 작업 내용
- Spring Boot 프로젝트 초기화 (`spring init`)
- DB 분리 설정
  - dev: MySQL (`application-dev.yml`)
  - test: H2 in-memory (`application-test.yml`)
- User, Product 엔티티 및 Repository 작성
- JUnit + H2 기반 단위테스트 작성 (`UserRepositoryTest`, `ProductRepositoryTest`)

### 문제 및 해결 과정
1. **테스트 클래스 경로 문제**
   - 오류: `Unable to find a @SpringBootConfiguration`
   - 원인: 테스트 파일을 `src/main/java`에 두었음
   - 해결: `src/test/java` 하위로 옮김

2. **Autowired 파라미터 주입 실패**
   - 오류: `No ParameterResolver registered for parameter ...`
   - 원인: 생성자 주입 방식에서 스프링 컨텍스트가 제대로 ProductRepository를 주입하지 못함
   - 해결: `@Autowired` 필드 주입 방식으로 수정

3. **schema.sql 문법 에러 (MySQL vs H2 차이)**
   - 오류: `JdbcSQLSyntaxErrorException: Syntax error in SQL statement ... TINYINT(1)`
   - 원인: MySQL에서는 `TINYINT(1)`이 boolean처럼 동작하지만, H2는 인식 못함
   - 해결: 
     - 테스트용 DB는 `hibernate.ddl-auto=create-drop`으로 자동 생성하도록 설정
     - `application-test.yml`에 `spring.sql.init.mode=never` 추가해 `schema.sql` 무시

### 배운 점
- JUnit 테스트 환경은 반드시 **`src/test/java`** 폴더에서 관리해야 한다.  
- H2는 MySQL 호환 모드를 켜도 100% 동일하지 않으므로, 테스트 시 스키마 자동 생성 전략을 쓰는 게 편리하다.  
- 테스트 환경과 개발 환경을 **프로파일 분리(dev/test)** 해서 관리하면 디버깅 시간이 크게 줄어든다.

---
