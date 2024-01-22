CREATE TABLE achiever
(
    id      VARCHAR(64) DEFAULT random_uuid() PRIMARY KEY,
    name    VARCHAR(256) NOT NULL,
    email   VARCHAR(256) NOT NULL UNIQUE
);

CREATE TABLE objective
(
    id          VARCHAR(64) DEFAULT random_uuid() PRIMARY KEY,
    title       VARCHAR(512) NOT NULL,
    achiever_id VARCHAR(64) NOT NULL,
    FOREIGN KEY (achiever_id) REFERENCES achiever(id)
);