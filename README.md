# File Uploader API
    It's a sample of service with a exposed controller to receive file and transform into zip file

## Requirements
    Java: JDK 17
    Gradle
    Docker

### How to run tests
* Run commands on terminal inside project folder:
    ```
    ./gradlew clean test
    ```

### How to build and run application
You can choose build and run the application directly, using Gradle or using Docker.
Run commands on terminal inside project folder:

#### Gradle
* Using Gradle to build and run application:
    1. Build and run
        ```
        ./gradlew clean build bootRun
        ```
    2. To stop, press CTRL+C or respective
#### Docker
* Using Gradle to build and Docker to run application:
    1. Build with Gradle
        ```
        ./gradlew clean build
       ```
    2. Run on Docker
        1. Build docker image
        ```
        docker build -t file-uploader:latest .
        ```
        2. Run docker image
        ```
        docker run -p 8080:8080 -t file-uploader:latest
        ```
        3. To stop, press CTRL+C or respective

#### Documentation in `Swagger`:
1. Open your browser
2. Type in URL bar `http://{app-ip-adress}:8080/swagger-ui.html`

