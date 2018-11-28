CREATE SCHEMA `bbwsso` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;

CREATE TABLE `bbwsso`.`application` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `app_id` VARCHAR(50) NOT NULL,
  `public_key` LONGTEXT NOT NULL,
  PRIMARY KEY (`id`));

  ALTER TABLE `bbwsso`.`application` 
CHANGE COLUMN `public_key` `certificate` BLOB NOT NULL ;

ALTER TABLE `bbwsso`.`application` 
CHANGE COLUMN `certificate` `certificate` BLOB NULL ;

ALTER TABLE `bbwsso`.`application` 
CHANGE COLUMN `certificate` `x509_certificate` BLOB NULL DEFAULT NULL ;


CREATE TABLE `bbwsso`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `apps` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);