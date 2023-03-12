CREATE TABLE brands
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE categories
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE models
(
    id       BIGSERIAL PRIMARY KEY,
    name     TEXT NOT NULL UNIQUE,
    brand_id BIGINT REFERENCES brands (id) ON DELETE CASCADE
);

CREATE TABLE cars
(
    id                  BIGSERIAL PRIMARY KEY,
    brand_id            BIGINT REFERENCES brands (id) ON DELETE CASCADE,
    year_of_manufacture DATE NOT NULL,
    model_id            BIGINT REFERENCES models (id) ON DELETE CASCADE
);
CREATE TABLE cars_categories
(
    car_id      BIGINT REFERENCES cars (id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    PRIMARY KEY (car_id, category_id)
);