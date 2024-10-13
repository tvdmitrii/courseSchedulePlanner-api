# Development Log

### Week 1 - (08/26/2024 - 09/01/2024) - ~ 1 hour
- Identified the project goal and scope.

### Week 2 - (09/02/2024 - 09/08/2024) - ~ 3 hours
- Set up the project repository and documentation.
- Thinking about database design.
- Thinking about page design.
- Thinking about user stories.
- Thinking about application architecture.

### Week 3 - (09/09/2024 - 09/15/2024) - ~ 8 hours
- Completed all the page mockups.
- Completed user stories.
- Completed project draft plan.
- Ready for Checkpoint 1.

### Week 4 - (09/16/2024 - 09/22/2024) - ~ 6 hours
- Completed database design.
- Integrated log4j logging.
- Integrated Hibernate ORM.
- Created User entity and UserDao.
- Created tests for User and UserDao.

### Week 5 - (09/23/2024 - 09/29/2024) - ~ 10 hours
- Switched to using generic DAO.
- Added department and instructor DAO.
- Work on tests and JavaDoc.
- Implemented one-to-many relationship between department and courses.

### Week 6 - (09/30/2024 - 10/06/2024) - ~ 15 hours
- Original database design was inadequate. Fix the design to properly take into account adding course and sections to cart and building schedules.
- Switch tests to reset database before each test to fix repeatability issues.
- Fully implement DAO, DAO tests, and JavaDocs for them.

### Week 7 - (10/07/2024 - 10/13/2024) - ~ 15 hours
- Split the project into three repositories: WebApp, REST API, API interface.
- Implement REST API server using.
- Implement REST API client using.
- Added Maven filters to manage development and production builds.
- Set up AWS Elastic Beanstalk environment for hosting the database, REST API, and WebApp.
- Deploy the database, REST API, and WebApp to AWS.