DROP SCHEMA IF EXISTS `maply`;
CREATE SCHEMA `maply`;
USE `maply`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    UNIQUE(`email`, `username`),
    PRIMARY KEY (`id`)
);
INSERT INTO `user`(`username`,`first_name`,`last_name`,`email`)
    VALUES('Piotr', 'Piotr', 'Kowalski', 'piotr.kowalski@gmail.com');
INSERT INTO `user`(`username`,`first_name`,`last_name`,`email`)
    VALUES('Maciej', 'Maciej', 'Kwiatkowski', 'maciej.kwiatkowski@gmail.com');
INSERT INTO `user`(`username`,`first_name`,`last_name`,`email`)
    VALUES('Wojciech', 'Wojciech', 'Nowak', 'wojciech.nowak@gmail.com');
INSERT INTO `user`(`username`,`first_name`,`last_name`,`email`)
    VALUES('Anna', 'Anna', 'Wojciechowska', 'anna.wojciechowska@gmail.com');
INSERT INTO `user`(`username`,`first_name`,`last_name`,`email`)
    VALUES('Monika', 'Monika', 'Kalinowska', 'monika.kalinowska@gmail.com');

