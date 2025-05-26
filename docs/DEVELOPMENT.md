# Development Guide for Secure File Vault

## 🔧 Development Setup

### Prerequisites

1. **Java Development Kit (JDK) 22**
   - Download and install from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK
   - Set JAVA_HOME environment variable

2. **Maven**
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Add to system PATH

3. **IDE**
   - Recommended: IntelliJ IDEA or Eclipse with JavaFX plugin
   - Install Checkstyle and SpotBugs plugins

### Project Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/secure-file-vault.git
   cd secure-file-vault
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   ./run.bat    # Windows
   ./run.sh     # Linux/Mac
   ```

## 🏗️ Project Structure

```
secure-file-vault/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/securefilevault/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       ├── ui/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── fxml/
│   │       ├── css/
│   │       └── images/
│   └── test/
│       └── java/
├── config/
├── docs/
└── logs/
```

## 🔍 Code Quality

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and small

### Static Analysis

1. **Checkstyle**
   ```bash
   mvn checkstyle:check
   ```

2. **SpotBugs**
   ```bash
   mvn spotbugs:check
   ```

3. **JaCoCo Coverage**
   ```bash
   mvn test jacoco:report
   ```

## 🧪 Testing

### Unit Tests

- Write tests for all new features
- Maintain test coverage above 80%
- Use meaningful test names

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName
```

### UI Testing

- Use TestFX for JavaFX UI testing
- Test all user interactions
- Verify error handling

## 🔒 Security Guidelines

1. **Encryption**
   - Use AES-256 for file encryption
   - Generate unique IVs for each encryption
   - Use secure random for key generation

2. **Password Handling**
   - Use PBKDF2 for password hashing
   - Never store plain passwords
   - Implement rate limiting

3. **File Handling**
   - Validate file paths
   - Sanitize file names
   - Implement secure deletion

## 📝 Logging

- Use SLF4J with Logback
- Follow logging levels:
  - ERROR: Application errors
  - WARN: Unexpected situations
  - INFO: Important events
  - DEBUG: Development information

## 🚀 Release Process

1. **Version Update**
   - Update version in pom.xml
   - Update CHANGELOG.md

2. **Testing**
   - Run all tests
   - Perform manual testing
   - Check code coverage

3. **Build**
   ```bash
   mvn clean package
   ```

4. **Release**
   - Tag the release
   - Create GitHub release
   - Upload artifacts

## 🐛 Debugging

1. **Logging**
   - Check logs in `logs/secure-vault.log`
   - Enable debug logging in `logback.xml`

2. **Remote Debugging**
   ```bash
   java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
   ```

## 🔄 Continuous Integration

- GitHub Actions workflow
- Automated tests on pull requests
- Code quality checks
- Coverage reports

## 📚 Documentation

- Keep README.md updated
- Document API changes
- Update JavaDoc comments
- Maintain CHANGELOG.md

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes
4. Add tests
5. Submit pull request

Refer to CONTRIBUTING.md for detailed guidelines.