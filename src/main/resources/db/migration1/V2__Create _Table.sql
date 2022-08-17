SET search_path TO test_flyway_schema;

CREATE TABLE IF NOT EXISTS discount_card_flyway_test
(
    id     SERIAL PRIMARY KEY,
    number VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS products_flyway_test
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    price DECIMAL(8, 2) NOT NULL,
    discount BOOLEAN NOT NULL
);