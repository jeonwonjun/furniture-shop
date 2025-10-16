# 📘 Daily Log

> 프로젝트 진행 과정을 Day별로 기록합니다.  
> 문제 해결 과정과 배운 점도 함께 정리합니다.

---

## Day 1 (2025-09-21)

### ✅ 작업 내용
- Spring Boot 프로젝트 초기화 (`spring init`)
- DB 분리 설정
  - dev: MySQL (`application-dev.yml`)
  - test: H2 in-memory (`application-test.yml`)
- User, Product 엔티티 및 Repository 작성
- JUnit + H2 기반 단위테스트 작성 (`UserRepositoryTest`, `ProductRepositoryTest`)

### ⚡ 문제 및 해결 과정
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

### ✨ 배운 점
- JUnit 테스트 환경은 반드시 **`src/test/java`** 폴더에서 관리해야 한다.  
- H2는 MySQL 호환 모드를 켜도 100% 동일하지 않으므로, 테스트 시 스키마 자동 생성 전략을 쓰는 게 편리하다.  
- 테스트 환경과 개발 환경을 **프로파일 분리(dev/test)** 해서 관리하면 디버깅 시간이 크게 줄어든다.

---

## 📅 Day 2 (2025-09-22)

### ✅ 작업 내용
- **회원가입/로그인 (JWT 인증)**  
  - Spring Security + JWT 토큰 발급/검증 기능 구현  
  - 회원가입 시 비밀번호 Bcrypt 저장 로직 적용  

- **JUnit 테스트**  
  - UserRepository, ProductRepository, AuthController 테스트 작성  

- **상품 API**  
  - `/api/products`, `/api/products/{id}` 구현 및 테스트 데이터 확인  

- **프론트엔드 연동**  
  - React + Vite 세팅 후 상품 목록/상세 페이지와 백엔드 API 연결  


### ⚡ 문제 & 해결 과정
1. **테스트 오류 (401 대신 404 발생)**
   -  원인: SecurityConfig 설정과 MockMvc 요청 경로 불일치
   -  해결: Security 설정 조정 및 테스트 기대값 수정 → 정상적으로 401/200 확인  

2. **프론트엔드 빈 화면**
   - 원인: 초기 라우팅/컴포넌트 구조가 제대로 연결되지 않음
   - 해결: `main.tsx` + `App.tsx` 구조 정리, axios로 API 호출 후 목록 렌더링 성공  

3. **Node.js 버전 문제 (crypto.hash 에러)**
   - 원인: Vite 6가 Node.js 20.19+ 이상 필요, 설치된 버전은 20.10.0
   - 해결: Node.js 22.12.0 설치 후 정상 실행  


### ✨ 오늘의 성과
- 회원가입/로그인부터 상품 조회까지 **백엔드–프런트엔드 전체 흐름 완성**  
- 주요 에러를 직접 해결하면서 **Spring Security 인증 흐름**과 **React-Vite 환경 구성**에 대한 이해도 상승  

---

## 📅 Day 3 (2025-09-23)


### ✅ 작업 내용

### GitHub Actions CI 세팅
- `.github/workflows/ci.yml` 작성
- `main` 브랜치 푸시 시 **Gradle 빌드 + JUnit 테스트** 실행
- 테스트 리포트 업로드 구성
- (추후) 프론트엔드 빌드 job 추가 예정

### 장바구니 기능
- **API (`/api/cart`)**
  - `POST /api/cart` : 장바구니 업서트(지정 수량)
  - `GET /api/cart`  : 로그인 사용자 장바구니 조회
  - `DELETE /api/cart/{itemId}` : 아이템 삭제
- **DB**
  - `cart_items (id, user_id, product_id, quantity, created_at)`

---

### ⚡ 문제 & 해결 (기능/DB 중심)

1. 테스트 DB 불일치(H2 ↔ MySQL)로 CI 실패
- 로컬은 H2, CI는 MySQL로 돌려 **쿼리/DDL 차이**로 테스트 실패.
- **해결:** `test` 프로필을 **MySQL**로 통일해 로컬/CI 동일 환경으로 검증.  
  *(대안: Testcontainers MySQL 도입 검토)*

2. 리포지토리 JPQL 오타/바인딩 오류
- 엔티티명 오타(`CarItem`)·파라미터 이름 불일치(`:itemId`)로 **컨텍스트 로딩/삭제 실패**.
- **해결:** `@Query` 제거하고 **파생 쿼리**로 전환  
  `findByUserIdAndProductId(...)`, `findByUserIdAndId(...)`.

3. “상품이 존재하지 않습니다.” 예외
- 장바구니 담기 시 대상 `product_id`가 **products**에 없음.
- **해결:** 테스트 전에 **상품 시드** 삽입(`@BeforeEach save(...)`, 반환된 **id 동적 사용**.

4. 삭제 권한 검증
- 임의 `itemId`로 타 유저의 아이템 삭제 가능성.
- **해결:** 조회에 **소유자 조건 포함**  
  `findByUserIdAndId(userId, itemId)`로 존재·소유 동시 확인 후 삭제.

> **권장 제약/인덱스**
> ```sql
> ALTER TABLE cart_items
>   ADD CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id);
> CREATE INDEX idx_cart_user ON cart_items(user_id);
> ```

---

### ✨ 오늘의 성과
- 로컬/CI **테스트 환경 일치** → 파이프라인 안정화
- 리포지토리 **파생 쿼리 전환** → 컨텍스트 로딩 오류 제거
- 테스트 **데이터 시드 표준화**로 재현성↑
- 장바구니 핵심 플로우(담기→조회→업서트→삭제) 정상 동작 검증

---

## 4) 다음 할 일
- **Testcontainers(MySQL)** 도입으로 로컬/CI 완전 일치 환경 구축
- 전역 예외 처리(`@RestControllerAdvice`)로 에러 응답 포맷 통일(예: 404/400 JSON)
- 수량 검증 강화(`quantity >= 1`): DB 제약 또는 서비스 레벨 검증
- 헤더에 장바구니 뱃지(총 수량/금액) 노출, 주문 플로우 초안 연결
- ### 참고 메모
- 인증 의존 테스트는 `@WithMockUser(username = "1")`으로 **userId=1** 가정 일관화
- 자동 증가 ID는 **하드코딩 금지**, `save(...)` 반환 ID 사용

---

## 📅 Day 4 (2025-10-14)
연휴 및 AWS 배포 테스트 문제로 2주간 막히다보니 프로젝트 진전이 없었다.
그리고 기존에 ChatGPT와 '함께' 프로젝트를 제작하다 보니 공부하는 부분에 있어서 아쉬운 부분을 인정한다.

오늘부터는 AI 없이, 또는 '참고'하여 기본부터 최소 단위로 구축 후 빠르게 배포하는 방향으로 진행할 예정이다.

1. [요구사항 분석](https://docs.google.com/spreadsheets/d/1T7nwAaSk0EeqetnZjxFocVZTuovcr06tu_AHs67Cvio/edit?gid=0#gid=0)
2. 기능 명세서 작성
3. API 설계
4. 아키텍처 설계
5. 최소 기능 구현
6. CI/CD

순서로 빠르게 MVP 배포 후 리팩토링 및 확장을 진행하겠다.

---







