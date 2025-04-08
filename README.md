# 🔐 Secure File Vault System

A secure desktop application to **encrypt and decrypt files**, built with **Java 20**, **JavaFX**, and **AES encryption**. It features a modern GUI, secure user authentication, and is extendable for cloud syncing and audit logging.

---

## 🚀 Features (Initial Version)

- 🔒 **AES File Encryption/Decryption**  
  Encrypt your files using AES-256 with password-based key derivation. Decryption is allowed only after successful login, ensuring strong protection against unauthorized access.

- 👤 **User Authentication**  
  Secure registration and login system with password hashing (PBKDF2). Session management maintains user context while ensuring safety.

> 🛠️ _Planned features_:  
> - Folder-tree view for exploring encrypted directories  
> - Audit logs to monitor access and changes  
> - AWS S3 cloud sync support

---

## 🧰 Tech Stack

| Component   | Technology                     |
|-------------|-------------------------------|
| Language     | Java 20 (with preview features) |
| GUI          | JavaFX 24                     |
| Encryption   | Java Cryptography API (AES)   |
| Database     | SQLite (MySQL supported)      |
| Build Script | Custom `build.bat` script     |

---

## 🗂️ Project Structure

SecureFileVault/ ├── src/ │ └── main/ │ ├── java/ │ │ └── com/freelancetracker/ │ │ ├── Main.java │ │ ├── controllers/ │ │ ├── models/ │ │ └── services/ │ └── resources/ ├── lib/ │ ├── javafx-sdk-24/ │ ├── sqlite-jdbc-3.49.1.0.jar │ └── pdfbox-3.0.4.jar ├── out/ │ └── (compiled output) ├── build.bat └── README.md


---

## 🏁 Getting Started

### ✅ Prerequisites

- Java 20 installed and `JAVA_HOME` configured correctly  
- JavaFX SDK 24 ([Download](https://gluonhq.com/products/javafx/))  
- SQLite JDBC Driver ([Download](https://github.com/xerial/sqlite-jdbc/releases))  
- (Optional) Apache PDFBox ([Download](https://pdfbox.apache.org/download.html))

### ▶️ Run the App

To compile and run the application:

```bash
.\build.bat
