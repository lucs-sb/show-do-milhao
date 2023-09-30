CREATE TABLE tb_role (
	role_id TINYINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	login_id BIGINT NOT NULL,
	name VARCHAR(50) NOT NULL
) ENGINE = innodb;

CREATE TABLE tb_login (
	login_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_account_id BIGINT NOT NULL,
	nickname VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL,
    deletion_date DATE NULL
) ENGINE = innodb;

CREATE TABLE tb_user_account (
	user_account_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250) NOT NULL,
	nickname VARCHAR(250) NOT NULL,
    avatar VARCHAR(250) NULL,
    deletion_date DATE NULL
) ENGINE = innodb;

ALTER TABLE tb_role ADD CONSTRAINT fk_role_login_id FOREIGN KEY(login_id) REFERENCES tb_login(login_id);
ALTER TABLE tb_login ADD CONSTRAINT fk_login_user_account_id FOREIGN KEY(user_account_id) REFERENCES tb_user_account(user_account_id);

CREATE TABLE tb_question (
	question_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_account_id BIGINT NOT NULL,
	statement VARCHAR(250) NOT NULL,
    accepted BIT NOT NULL,
	amount_approvals TINYINT NOT NULL,
	amount_complaints TINYINT NOT NULL,
	amountFailures TINYINT NOT NULL,
    deletion_date DATE NULL
) ENGINE = innodb;

ALTER TABLE tb_question ADD CONSTRAINT fk_question_user_account_id FOREIGN KEY(user_account_id) REFERENCES tb_user_account(user_account_id);

CREATE TABLE tb_answer (
	answer_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(250) NOT NULL,
	correct BIT NOT NULL
) ENGINE = innodb;

CREATE TABLE tb_question_answer (
	question_id BIGINT NOT NULL,
	answer_id BIGINT NOT NULL
) ENGINE = innodb;

ALTER TABLE tb_question_answer ADD CONSTRAINT fk_question_answer_question_id FOREIGN KEY(question_id) REFERENCES tb_question(question_id);
ALTER TABLE tb_question_answer ADD CONSTRAINT fk_question_answer_answer_id FOREIGN KEY(answer_id) REFERENCES tb_answer(answer_id);

CREATE TABLE tb_validation_question_user (
	user_account_id BIGINT NOT NULL,
	question_id BIGINT NOT NULL
) ENGINE = innodb;

ALTER TABLE tb_validation_question_user ADD CONSTRAINT fk_validation_question_user_question_id FOREIGN KEY(question_id) REFERENCES tb_question(question_id);
ALTER TABLE tb_validation_question_user ADD CONSTRAINT fk_validation_question_user_user_account_id FOREIGN KEY(user_account_id) REFERENCES tb_user_account(user_account_id);

CREATE TABLE tb_validated_questions_user (
	user_account_id BIGINT NOT NULL,
	question_id BIGINT NOT NULL
) ENGINE = innodb;

ALTER TABLE tb_validated_questions_user ADD CONSTRAINT fk_validated_questions_user_question_id FOREIGN KEY(question_id) REFERENCES tb_question(question_id);
ALTER TABLE tb_validated_questions_user ADD CONSTRAINT fk_validated_questions_user_user_account_id FOREIGN KEY(user_account_id) REFERENCES tb_user_account(user_account_id);

CREATE TABLE tb_match (
	match_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_account_id BIGINT NOT NULL,
	award INT NULL,
	ended BIT NOT NULL,
	last_question_answered TINYINT NULL,
	deleted_answers BIT NOT NULL,
	reason_for_closing VARCHAR(250) NULL
) ENGINE = innodb;

ALTER TABLE tb_match ADD CONSTRAINT fk_match_user_account_id FOREIGN KEY(user_account_id) REFERENCES tb_user_account(user_account_id);

CREATE TABLE tb_match_question (
	match_id BIGINT NOT NULL,
	question_id BIGINT NOT NULL,
	position TINYINT NOT NULL
) ENGINE = innodb;

ALTER TABLE tb_match_question ADD CONSTRAINT fk_match_question_match_id FOREIGN KEY(match_id) REFERENCES tb_match(match_id);
ALTER TABLE tb_match_question ADD CONSTRAINT fk_match_question_question_id FOREIGN KEY(question_id) REFERENCES tb_question(question_id);

INSERT INTO tb_user_account VALUES ('1', '', '', NULL, NULL);
INSERT INTO tb_login VALUES (1, 1, '', '', NULL);
INSERT INTO tb_role VALUES (1, 1, 'ADMIN');