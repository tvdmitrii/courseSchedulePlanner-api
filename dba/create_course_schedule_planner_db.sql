-- Drop tables if exist
DROP TABLE IF EXISTS `schedule_section`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `cart_course_section`;
DROP TABLE IF EXISTS `section`;
DROP TABLE IF EXISTS `cart_course`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `instructor`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `user`;

-- Create tables
CREATE TABLE `course` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128) NOT NULL,
  `description` TEXT NOT NULL,
  `credits` TINYINT UNSIGNED NOT NULL,
  `number` SMALLINT UNSIGNED NOT NULL,
  `department_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `department` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `instructor` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` varchar(64)  NOT NULL,
  `last_name` varchar(64)  NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `section` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `days_of_week` TINYINT UNSIGNED NOT NULL,
   `from_time` TIME NOT NULL,
   `to_time` TIME NOT NULL,
   `instructor_id` BIGINT UNSIGNED NOT NULL,
   `course_id` BIGINT UNSIGNED NOT NULL,
   PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `first_name` varchar(64) NOT NULL,
    `last_name` varchar(64) NOT NULL,
    `email` varchar(128) NOT NULL,
    `role` TINYINT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `cart` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `cart_course` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `cart_id` BIGINT UNSIGNED NOT NULL,
   `course_id` BIGINT UNSIGNED NOT NULL,
   PRIMARY KEY (`id`)
);

CREATE TABLE `cart_course_section` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `cart_course_id` BIGINT UNSIGNED NOT NULL,
   `section_id` BIGINT UNSIGNED NOT NULL,
   PRIMARY KEY (`id`)
);

CREATE TABLE `schedule` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `selected` BIT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `schedule_section` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `schedule_id` BIGINT UNSIGNED NOT NULL,
    `section_id` BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`)
);

-- Foreign Keys
ALTER TABLE `course` ADD CONSTRAINT `course_department` FOREIGN KEY `course_department` (`department_id`)
    REFERENCES `department` (`id`);
ALTER TABLE `cart` ADD CONSTRAINT `cart_user` FOREIGN KEY `cart_user` (`user_id`)
    REFERENCES `user` (`id`);
ALTER TABLE `cart_course` ADD CONSTRAINT `cart_course_cart` FOREIGN KEY `cart_course_cart` (`cart_id`)
    REFERENCES `cart` (`id`);
ALTER TABLE `cart_course` ADD CONSTRAINT `cart_course_course` FOREIGN KEY `cart_course_course` (`course_id`)
    REFERENCES `course` (`id`);
ALTER TABLE `cart_course_section` ADD CONSTRAINT `cart_course_section_cart_course` FOREIGN KEY `cart_course_section_cart_course` (`cart_course_id`)
    REFERENCES `cart_course` (`id`);
ALTER TABLE `cart_course_section` ADD CONSTRAINT `cart_course_section_section` FOREIGN KEY `cart_course_section_section` (`section_id`)
    REFERENCES `section` (`id`);
ALTER TABLE `schedule` ADD CONSTRAINT `schedule_user` FOREIGN KEY `schedule_user` (`user_id`)
    REFERENCES `user` (`id`);
ALTER TABLE `schedule_section` ADD CONSTRAINT `schedule_section_schedule` FOREIGN KEY `schedule_section_schedule` (`schedule_id`)
    REFERENCES `schedule` (`id`);
ALTER TABLE `schedule_section` ADD CONSTRAINT `schedule_section_section` FOREIGN KEY `schedule_section_section` (`section_id`)
    REFERENCES `section` (`id`);
ALTER TABLE `section` ADD CONSTRAINT `section_course` FOREIGN KEY `section_course` (`course_id`)
    REFERENCES `course` (`id`);
ALTER TABLE `section` ADD CONSTRAINT `section_instructor` FOREIGN KEY `section_instructor` (`instructor_id`)
    REFERENCES `instructor` (`id`);

-- Unique
ALTER TABLE `course` ADD UNIQUE `uq_course_department_number`(`department_id`,`number`);
ALTER TABLE `department` ADD UNIQUE `uq_department_name`(`name`);
ALTER TABLE `department` ADD UNIQUE `uq_department_code`(`code`);
ALTER TABLE `instructor` ADD UNIQUE `uq_instructor_name`(`first_name`,`last_name`);
ALTER TABLE `user` ADD UNIQUE `uq_user_email`(`email`);
ALTER TABLE `cart` ADD UNIQUE `uq_cart_user`(`user_id`);

-- Checks
ALTER TABLE `section` ADD CONSTRAINT `section_valid_days_mask` CHECK (`days_of_week` < 32);
ALTER TABLE `course` ADD CONSTRAINT `course_valid_credits` CHECK (`credits` < 10);
ALTER TABLE `user` ADD CONSTRAINT `user_valid_role` CHECK (`role` < 2);

-- Index
CREATE INDEX `idx_course_title` ON `course` (`title`); 
