CREATE TABLE users (
	user_id	      INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email         VARCHAR(128) NOT NULL UNIQUE,
    full_name     VARCHAR(64)  NOT NULL,
    date_of_birth DATETIME     NOT NULL,
    creation_time DATETIME     NOT NULL,
    password      VARCHAR(128) NOT NULL,
    password_salt VARCHAR(128) NOT NULL
)