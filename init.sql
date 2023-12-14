CREATE TABLE assets (
    id INT,
    name VARCHAR(255),
    is_promoted BOOL,
    parent_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_id) REFERENCES assets(id)
);

INSERT INTO assets(id, name) VALUES
(1, 'A'),
(2, 'B'),
(3, 'C');
