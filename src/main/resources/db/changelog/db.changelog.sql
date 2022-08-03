-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE IF NOT EXISTS discount_card_try
(
    id     SERIAL PRIMARY KEY,
    number VARCHAR(64) NOT NULL
);

INSERT INTO discount_card_try(number)
VALUES ('card-120'),
       ('card-121'),
       ('card-122'),
       ('card-123'),
       ('card-777');