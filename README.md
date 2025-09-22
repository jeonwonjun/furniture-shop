# Furniture Shop (Spring Boot)

[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)](https://github.com/your-username/furniture-shop/actions) <!-- GitHub Actions 등에 연결하시면 좋아요 -->
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE) <!-- LICENSE 파일이 있다면 연결 -->

간단한 가구 쇼핑몰 백엔드 시작 프로젝트입니다.  
현재 Day 1까지 **도메인(User, Product)**, **MySQL + H2 환경**, **JPA 리포지토리**, **기본 보안 설정** 및 **단위 테스트**가 구성되었습니다.

---

## Tech Stack

*   **Language**: Java 17
*   **Build**: Gradle 8.14.3
*   **Framework**: Spring Boot 3.5.6
    *   `spring-boot-starter-web`
    *   `spring-boot-starter-data-jpa`
    *   `spring-boot-starter-security`
*   **DB**: MySQL 8.x (개발), H2 (테스트)
*   **ORM**: JPA (Hibernate)
*   **Test**: JUnit 5, AssertJ, Spring Boot Test
*   **Others**: HikariCP

---

## 📆 진행 상황
Day별 진행 로그는 [📘 Daily Log](docs/daily-log.md)에서 확인할 수 있습니다.

---
## 플로우차트
### &emsp;&emsp;&emsp;&emsp;&emsp;회원가입 로그인 &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;로그인 & JWT발급
<img width="320" height="640" alt="회원가입 플로우차트" src="https://github.com/user-attachments/assets/9fc2629a-7e90-4108-b9d4-8ce95ffa00c9" />
<img width="640" height="640" alt="로그인 JWT발급 플로우차트" src="https://github.com/user-attachments/assets/98e75eb0-cb65-43e4-aaf5-98bf5651598c" />

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;JWT를 이용한 요청
<img width="640" height="640" alt="JWT를 이용한 요청" src="https://github.com/user-attachments/assets/626654ad-79a1-4f54-85c5-b66d06cb8501" />
