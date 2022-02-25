# GIMS - Gypsy Inn Management System
## Group Project for CIS-3365

This is a sort of asset management system for a hotel based in Galveston, TX - The Queen Gypsy Inn - who were the teams sponsors for the project. The application - "GIMS" provides the following functionalities:

  - Room Vacancy Management
  - Housekeeping Status
  - Custom Event Registration
  - Customer Order Management
  - Employee Task Assignment per Room
  - Employee Scheduling
  - Employee Clock-in/Clock-out
  - Generation of a couple reports
 
 The UI was designed with material design in mind using jfoenix components.

## Technologies Used
- Spring Boot
  - Spring Data
  - Spring Security
- JavaFX
- MSSQL

## Setup
Set up requires the creation of an application.properties file in `src/main/resources` containing database connection information. You might also want to define the following fields:

  - Web server is not currently required so:
    - `spring.main.web-application-type = none`
  - If you want all tables to be automatically generated: (Keep in mind you will need to provide your MSSQL user the appropriate permissions)
    - `spring.jpa.hibernate.ddl-auto = update`

You will also need to make sure you are running a JDK with JavaFX bundled, or install JavaFX Seperately
The application was built using Java and JavaFX versions 11 

Finally, you will need to allow Maven to build the remaining dependencies before compiling and running the project. Alternativley, you can run `mvn clean package` to create and executable .jar file of the application.
