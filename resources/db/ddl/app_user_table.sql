CREATE TABLE users (
	  user_id	       INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email          VARCHAR(128) NOT NULL UNIQUE,
    first_name     VARCHAR(32)  NOT NULL,
    last_name      VARCHAR(32)  NOT NULL,
    date_of_birth  DATETIME     NOT NULL,
    creation_time  DATETIME     NOT NULL,
    password       VARCHAR(128) NOT NULL,
    password_salt  VARCHAR(128) NOT NULL,
    account_status VARCHAR(16)  NOT NULL
)