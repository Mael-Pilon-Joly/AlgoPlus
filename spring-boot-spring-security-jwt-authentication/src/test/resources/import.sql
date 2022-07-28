CREATE TABLE IF NOT EXISTS roles (
    id int(11) NOT NULL AUTO_INCREMENT,
    name varchar(20),
    PRIMARY KEY(id)
);

INSERT INTO `roles`
SET `id` = 1,
`name` =  'ROLE_ADMIN';

INSERT INTO `roles`
SET `id` = 2,
`name` =  'ROLE_USER';
