# 🔒 Secure File Vault System

A secure file management system that provides end-to-end encryption for your sensitive files. Built with Java and JavaFX, this application ensures your data remains protected with industry-standard encryption algorithms.

## ✨ Features

### Current Features
- 🔐 **Secure User Authentication**
  - PBKDF2 password hashing with salt
  - Secure session management
  - User registration and login system

- 🗄️ **File Management**
  - AES-256 encryption for file protection
  - Secure file storage and retrieval
  - File list management interface

- 🎨 **User Interface**
  - Modern JavaFX GUI
  - Intuitive navigation between screens
  - Responsive design with error handling

### Future Enhancements
- 🔄 **Advanced File Operations**
  - Batch file encryption/decryption
  - File compression before encryption
  - Cloud storage integration

- 🔐 **Enhanced Security**
  - Two-factor authentication (2FA)
  - Biometric authentication support
  - Password strength indicators
  - Session timeout and auto-logout

- 📱 **User Experience**
  - Dark/Light theme support
  - Drag-and-drop file support
  - Progress indicators for file operations
  - File preview capabilities

- 🔍 **Additional Features**
  - File sharing with other users
  - File version history
  - Secure file deletion
  - Activity logs and audit trails

## 🛠️ Tech Stack

- **Language**: Java 22
- **Framework**: JavaFX 24
- **Database**: SQLite
- **Encryption**: AES-256, PBKDF2
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 22 or higher
- Maven 3.8 or higher
- JavaFX 24
- SQLite JDBC driver

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/hreger/secure-file-vault.git
   cd secure-file-vault
   ```

2. **Build the project (Windows)**
   ```bash
   .\build.bat
   ```

3. **Run the application (Windows)**
   ```bash
   .\run.bat
   ```

## 🏗️ Project Structure

```
secure-file-vault/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/securefilevault/
│   │   │       ├── controllers/
│   │   │       ├── services/
│   │   │       ├── Main.java
│   │   │       └── module-info.java
│   │   └── resources/
│   │       └── fxml/
├── pom.xml
├── build.bat
└── run.bat
```

ScreenShots:

## 🔧 Configuration

The project's configuration and key scripts include:
- `pom.xml` - Maven project configuration
- `build.bat` - Build script for Windows
- `run.bat` - Run script for Windows

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- JavaFX team for the excellent GUI framework
- SQLite for the lightweight database solution
- All contributors and users of this project

---

Made with ❤️ by [P Sanjeev Pradeep]
