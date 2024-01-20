CREATE TABLE achiever
(
    id      VARCHAR(64) DEFAULT random_uuid() PRIMARY KEY,
    name    VARCHAR(256),
    email   VARCHAR(256)
);

CREATE TABLE objective
(
    id          VARCHAR(64) DEFAULT random_uuid() PRIMARY KEY,
    title       VARCHAR(512),
    achiever_id VARCHAR(64)
);