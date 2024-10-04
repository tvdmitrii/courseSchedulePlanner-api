-- Drop tables if exist
DROP TABLE IF EXISTS `schedule_section`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `cart_course_section`;
DROP TABLE IF EXISTS `cart_course`;
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

CREATE TABLE `cart_course` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `user_id` BIGINT UNSIGNED NOT NULL,
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
ALTER TABLE `cart_course` ADD CONSTRAINT `cart_course_user` FOREIGN KEY `cart_course_user` (`user_id`)
    REFERENCES `user` (`id`);
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

-- Checks
ALTER TABLE `section` ADD CONSTRAINT `section_valid_days_mask` CHECK (`days_of_week` < 32);
ALTER TABLE `course` ADD CONSTRAINT `course_valid_credits` CHECK (`credits` < 10);
ALTER TABLE `user` ADD CONSTRAINT `user_valid_role` CHECK (`role` < 2);

-- Index
CREATE INDEX `idx_course_title` ON `course` (`title`);

INSERT INTO `user`
(`id`, `first_name`, `last_name`, `email`, `role`)
VALUES
(1, "Phyllis", "Martin", "phyllis.martin@example.com", 0),
(2, "Lucas", "Hawkins", "lucas.hawkins@example.com", 0),
(3, "Charlene", "Williamson", "charlene.williamson@example.com", 0),
(4, "Gene", "Hughes", "gene.hughes@example.com", 1),
(5, "Unused", "User", "unused.user@example.com", 0);

INSERT INTO `instructor`
(`id`, `first_name`, `last_name`)
VALUES
(1, "Mattie", "Carpenter"),
(2, "Clinton", "Edwards"),
(3, "Raymond", "Walker"),
(4, "Unused", "Instructor");

INSERT INTO `department`
(`id`, `code`, `name`)
VALUES
(1, "CS", "Computer Science"),
(2, "ENG", "Engineering"),
(3, "ENGL", "English"),
(4, "TEST", "Testing Department"),
(5, "UD", "Unused Department");

INSERT INTO `course`
(`id`, `title`, `description`, `credits`, `number`, `department_id`)
VALUES
(1, "Introduction to Databases", "An introduction to databases course explores the fundamental concepts of database design, management, and querying, with a focus on relational databases and SQL.", 3, 121, 1),
(2, "Introduction to Programming", "An introduction to programming course teaches the basics of coding, problem-solving, and algorithmic thinking, using a beginner-friendly language to build foundational skills in software development.", 3, 101, 1),
(3, "Advanced Python", "An advanced Python course dives into complex programming concepts, including object-oriented design, multithreading, data structures, and advanced libraries, empowering students to build efficient, scalable applications.", 4, 202, 1),
(4, "Intermediate Themodynamics", "An intermediate thermodynamics course focuses on the application of the laws of thermodynamics to real-world systems, exploring energy transfer, entropy, and thermodynamic cycles in greater depth.", 3, 563, 2),
(5, "Fluid Mechanics", "A fluid mechanics course examines the behavior of fluids in motion and at rest, covering fundamental principles such as fluid dynamics, pressure, viscosity, and flow through various systems", 3, 363, 2),
(6, "Composition I", "A Composition I course focuses on developing foundational writing skills, emphasizing clear and effective communication, critical thinking, and the construction of well-organized essays.", 4, 101, 3),
(7, "Test Course 1", "", 3, 100, 4),
(8, "Test Course 2", "", 3, 200, 4);

INSERT INTO `section`
(`id`, `days_of_week`, `from_time`, `to_time`, `instructor_id`, `course_id`)
VALUES
(1, 5, "12:30", "14:10", 1, 1),
(2, 10, "12:30", "14:10", 1, 1),
(3, 5, "14:30", "16:10", 2, 1),
(4, 21, "8:30", "9:30", 2, 2),
(5, 10, "10:00", "12:00", 2, 2),
(6, 10, "14:00", "16:00", 1, 3),
(7, 10, "14:00", "16:00", 2, 3),
(8, 25, "11:30", "13:00", 2, 4),
(9, 21, "7:45", "9:00", 3, 5),
(10, 2, "16:00", "18:00", 3, 6);

INSERT INTO `cart_course`
(`id`, `user_id`, `course_id`)
VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3);

INSERT INTO `cart_course_section`
(`id`, `cart_course_id`, `section_id`)
VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 4),
(4, 3, 6),
(5, 3, 7);

INSERT INTO `schedule`
(`id`, `user_id`, `selected`)
VALUES
(1, 1, 0),
(2, 1, 0),
(3, 3, 0);

INSERT INTO `schedule_section`
(`id`, `schedule_id`, `section_id`)
VALUES
(1, 1, 1),
(2, 1, 10),
(3, 2, 4),
(4, 2, 3),
(5, 3, 8);

