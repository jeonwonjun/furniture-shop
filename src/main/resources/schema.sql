-- users 테이블 (개발 테스트용)
CREATE TABLE IF NOT EXISTS users (
  id           BIGINT       NOT NULL AUTO_INCREMENT,
  username     VARCHAR(50)  NOT NULL UNIQUE,
  password     VARCHAR(100) NOT NULL,
  role         VARCHAR(50)  NOT NULL DEFAULT 'USER',
  enabled      TINYINT(1)   NOT NULL DEFAULT 1,
  created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- (선택) 샘플 유저 한 명 넣기
-- 비밀번호를 나중에 Spring Security(BCrypt)로 맞출 거면 해시로 교체하세요.
-- INSERT INTO users (username, password, role, enabled) VALUES ('test', '{noop}pass', 'USER', 1);
