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
