CREATE TABLE shop
(
  id INT(11) PRIMARY KEY NOT NULL,
  name VARCHAR(64)
);

CREATE TABLE category
(
  id INT(11) PRIMARY KEY NOT NULL,
  title VARCHAR(64),
  shop_id INT(11) NOT NULL,
  CONSTRAINT shop_fk FOREIGN KEY (shop_id) REFERENCES shop (id)
);
CREATE INDEX shop_id_index ON category (shop_id);

CREATE TABLE product
(
  id INT(11) PRIMARY KEY NOT NULL,
  title VARCHAR(64),
  price DOUBLE,
  status VARCHAR(64) DEFAULT 'EXPECTED',
  category_id INT(11) NOT NULL,
  CONSTRAINT category_fk FOREIGN KEY (category_id) REFERENCES category (id)
);
CREATE INDEX category_id_index ON product (category_id);

INSERT INTO shop VALUE (1, 'Hardware Shop');
INSERT INTO shop VALUE (2, 'Software Shop');

INSERT INTO category VALUE (1, 'Notebooks', 1);
INSERT INTO category VALUE (2, 'Smartphone', 1);
INSERT INTO category VALUE (3, 'OS', 2);
INSERT INTO category VALUE (4, 'Drivers', 2);

INSERT INTO product VALUES (1, 'ASUS', 4500.00, 'AVAILABLE', 1);
INSERT INTO product VALUES (2, 'Lenovo', 4000.00, 'EXPECTED', 1);
INSERT INTO product VALUES (3, 'Samsung Galaxy', 150.00, 'AVAILABLE', 2);
INSERT INTO product VALUES (4, 'Windows 10', 200.00, 'ABSENT', 3);
