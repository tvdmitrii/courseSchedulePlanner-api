-- Drop tables if exist
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `section`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `instructor`;
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
    `name` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `instructor` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `first_name` varchar(64)  NOT NULL,
    `last_name` varchar(64)  NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `schedule` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `section_id` BIGINT UNSIGNED NOT NULL,
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

-- Foreign Keys
ALTER TABLE `course` ADD CONSTRAINT `course_department` FOREIGN KEY `course_department` (`department_id`)
    REFERENCES `department` (`id`);
ALTER TABLE `schedule` ADD CONSTRAINT `schedule_section` FOREIGN KEY `schedule_section` (`section_id`)
    REFERENCES `section` (`id`);
ALTER TABLE `schedule` ADD CONSTRAINT `schedule_user` FOREIGN KEY `schedule_user` (`user_id`)
    REFERENCES `user` (`id`);
ALTER TABLE `section` ADD CONSTRAINT `section_course` FOREIGN KEY `section_course` (`course_id`)
    REFERENCES `course` (`id`);
ALTER TABLE `section` ADD CONSTRAINT `section_instructor` FOREIGN KEY `section_instructor` (`instructor_id`)
    REFERENCES `instructor` (`id`);

-- Unique
ALTER TABLE `course` ADD UNIQUE `uq_course_department_number`(`department_id`,`number`);
ALTER TABLE `department` ADD UNIQUE `uq_department_name`(`name`);
ALTER TABLE `instructor` ADD UNIQUE `uq_instructor_name`(`first_name`,`last_name`);
ALTER TABLE `user` ADD UNIQUE `uq_user_email`(`email`);

-- Checks
ALTER TABLE `section` ADD CONSTRAINT `section_valid_days_mask` CHECK (`days_of_week` < 32);
ALTER TABLE `course` ADD CONSTRAINT `course_valid_credits` CHECK (`credits` < 10);
ALTER TABLE `user` ADD CONSTRAINT `user_valid_role` CHECK (`role` < 2); 

-- Index
CREATE INDEX `idx_course_title` ON `course` (`title`); 

