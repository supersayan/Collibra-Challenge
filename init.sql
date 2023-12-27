CREATE TABLE assets (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    is_promoted BOOL NOT NULL DEFAULT 0,
    parent_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES assets(id)
);

INSERT INTO assets(id, name) VALUES
(1, 'A'),
(2, 'B'),
(3, 'C');
