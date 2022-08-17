SET
search_path TO test_flyway_schema;

INSERT INTO discount_card_flyway_test(number)
VALUES ('card-120'),
       ('card-121'),
       ('card-122'),
       ('card-123'),
       ('card-777');

INSERT INTO products_flyway_test(title, price, discount)
VALUES ('Dress1_db', 10.11, false),
       ('Boots1_db', 25.33, true),
       ('Shoes1_db', 30.44, true),
       ('Jacket1_db', 35.55, true),
       ('Hat1_db', 140.66, true),
       ('Hat2_db', 40.77, true),
       ('West1_db', 45.88, false),
       ('West2_db', 45.99, true),
       ('Dress2_db', 15.00, true),
       ('Pants2_db', 20.11, true),
       ('Boots2_db', 25.22, false),
       ('Shoes2_db', 30.33, true),
       ('Jacket2_db', 35.44, true),
       ('Pants1_db', 10.22, false);
