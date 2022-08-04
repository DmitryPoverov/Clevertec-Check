CREATE SCHEMA test_flyway_schema;

SET search_path TO test_flyway_schema;

CREATE TABLE IF NOT EXISTS discount_card_flyway_test
(
    id     SERIAL PRIMARY KEY,
    number VARCHAR(64) NOT NULL
);

INSERT INTO discount_card_flyway_test(number)
VALUES ('card-120'),
       ('card-121'),
       ('card-122'),
       ('card-123'),
       ('card-777');