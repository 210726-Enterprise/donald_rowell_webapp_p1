# Web Application Example for Custom ORM 
## Project Description
A web application used to showcase the abilities of my custom object relational mapping (ORM) framework, written in Java, with a basic entity that is manipulated with HTTP requests made through Java Servlets which are translated into ORM methods. This application is set up for managing a user's bank account.
## Technologies Used
- Java 8
- JUnit 5
- Mockito
- Java EE Servlet API
- PostgreSql
- DBeaver
- AWS RDS
- Apache Maven
- Git SCM (on GitHub)

## Features
List of features ready:
- Able to perform simple CRUD operations (Insert, Select, Update, Delete)
- JDBC logic abstracted away by the custom ORM

To-do List:
- Update functionality as more features are added to the Custom ORM

## Getting Started
How to setup this code:
1. Download the project.
2. Install the Custom ORM found at https://github.com/210726-Enterprise/donald_rowell_orm_p1
3. Update the Maven dependency in the web application that uses the Custom ORM, editing the systemPath according to where you saved the file to:
      
            <dependency>
                  <groupId>com.revature</groupId>
                  <artifactId>Project01</artifactId>
                  <version>2.0</version>
                  <scope>system</scope>
                  <systemPath>${project.basedir}/Project01-2.0.jar</systemPath>
              </dependency>
3. Set system environment variables of "db_url", "db_username", and "db_password" to your database's url, username, and password, respectively.
4. The web application is now ready to showcase the Custom ORM
## Usage
How to use this code:
- Launch the code using a tomcat configuration.
- The methods available to be performed currently is POST, GET, PUT, and DELETE.
- For greater details on how this application works, there are JavaDocs describing each method in the application.
## Contributors
- [ ] Donald Rowell

