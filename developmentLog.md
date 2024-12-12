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
- Implement REST API server with Jersey.
- Implement REST API client with Jersey.
- Added Maven filters to manage development and production builds.
- Set up AWS Elastic Beanstalk environment for hosting the database, REST API, and WebApp.
- Deploy the database, REST API, and WebApp to AWS.

### Week 8 - (10/14/2024 - 10/20/2024) - ~ 15 hours
- Set up the HTML/CSS template for how the website will look like.
- Implemented one of the main pages that handles course browsing and search

### Week 9 - (10/21/2024 - 10/27/2024) - ~ 20 hours
- AWS Elastic Beanstalk retired Tomcat 8 which prompted an upgrade that has been long on my mind.
  - Java 11 -> Java 21 LTS
  - Tomcat 8 -> Tomcat 10.1
  - Java EE 7 -> Jakarta EE 10
  - JSTL 1.2 -> JSTL 3.0
  - Jersey 2.45 -> Jersey 3.1.9
  - Other minor updates
- Rolled out new Tomcat 10.1 environment on AWS EBS.
- Enabled HTTPs for the EBS environment to work with Cognito.
- Added course search by department and/or title functionality to course DAO and REST API.
- Updated database design to store user UUID from Cognito, but remove all other personally identifiable information.
- Added REST API user endpoint to handle new user creation and user search by UUID.
- Implemented proof of concept Cognito authentication integration. The provided code was significantly reworked to take advantage of Jersey Client for fetching user token and com.auth0 libraries for token parsing and validation.

### Week 10 - (10/28/2024 - 11/03/2024) - ~ 10 hours
- Wrap-up authentication.
- Began working on user course cart.

### Week 11 - (11/04/2024 - 11/10/2024) - ~ 10 hours
- Fully implemented course cart.
- Began working on admin course editing feature.
- Introduced navigation state and organized servlets.
- Added to restrict access to certain pages.

### Week 12 - (11/11/2024 - 11/17/2024) - ~ 20 hours
- Added ability for administrators to add/delete/edit courses.
- Refactored REST API to return response objects.
- Vastly improved logging and error handling.
- Added dynamic Bootstrap Toasts - pop-ups that notify user about success/failure of an operation.
- Added meeting day and time DTOs in preparation for section editing feature.
- Added course section and instructor resources.

### Week 13 - (11/18/2024 - 11/24/2024) - ~ 20 hours
- Added ability for administrators to add/delete/edit sections.
- Course and section editing pages were combined, because handling modal windows with servlets proved to be difficult.
- Instead of hiding pages from unauthorized users, all pages are visible in the navigation bar. If user does not have permission to access the page, they are sent to home page and a toast indicates the specific error.

### Week 14 - (11/25/2024 - 12/01/2024) - ~ 20 hours
- Added schedule generation functionality and ability to view schedules.
- Clean up code, add JavaDocs if missing.
- Update deployed version.
