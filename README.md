
# Spring Batch Microservice

This repository contains a Spring Batch microservice designed to process data from input CSV/TXT files, load it into a database, and generate an acknowledgment (`.ack`) file upon completion.

## Features
- **Spring Batch Integration**: Efficient batch processing framework for large-scale data handling.
- **Database Integration**: Supports MySQL for data persistence.
- **Log4j2 Logging**: Comprehensive logging for monitoring and debugging.
- **HikariCP Connection Pooling**: High-performance database connection management.
- **Ack File Generation**: Generates a `.ack` file after successful data load.

## Prerequisites
To run this project, ensure you have the following installed:
- Java 11 or later
- Maven
- MySQL Server
- Git

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```

### 2. Configure the Application
Edit the `src/main/resources/application.properties` file to match your database configuration:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.batch.initialize-schema=always
logging.level.root=INFO
```

### 3. Build the Project
Use Maven to build the project:
```bash
mvn clean install
```

### 4. Run the Application
Start the Spring Batch application:
```bash
mvn spring-boot:run
```

### 5. Verify Logs
Check the logs to confirm the application is running and processing files correctly. Logs will output to the console or to a file if configured in `log4j2.xml`.

## Logging Configuration
The project uses **Log4j2** for logging. You can customize the logging behavior by editing the `src/main/resources/log4j2.xml` file. Example configuration:
```xml
<Appenders>
  <Console name="Console" target="SYSTEM_OUT">
    <PatternLayout pattern="%d [%t] %-5level %logger{36} - %msg%n"/>
  </Console>
</Appenders>
```

## Job Workflow
1. **Input File Processing**: Reads data from a CSV or TXT file.
2. **Database Insertion**: Loads the data into a MySQL table.
3. **Ack File Generation**: Creates a `.ack` file to indicate job completion.

## Troubleshooting
- **Job Failure Logs**: Check the console or log files for error messages.
- **Database Connection Issues**: Ensure your database credentials and URL are correct.
- **File Not Processed**: Confirm the file path and format are valid.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Open a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact
For any questions or issues, please reach out to [your-email@example.com].
