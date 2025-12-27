# JavaFX User Authentication & Contact Management System

A modern desktop application built with JavaFX that provides user registration, authentication, and contact management functionality. The application features a clean, intuitive user interface with real-time input validation and secure user management.

## 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Application Flow](#application-flow)
- [Validation Rules](#validation-rules)
- [Data Storage](#data-storage)
- [Building and Running](#building-and-running)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### User Registration
- **Name, Email, and Password Registration**: Secure user account creation
- **Email Validation**: Real-time email format validation using regex patterns
- **Password Confirmation**: Ensures password accuracy through confirmation field
- **Duplicate User Prevention**: Checks for existing email addresses before registration
- **Real-time Input Validation**: Submit button enabled only when all fields are valid

### User Authentication
- **Secure Login**: Email and password-based authentication
- **Credential Validation**: Verifies user credentials against stored data
- **Error Handling**: Clear error messages for invalid credentials
- **Automatic Navigation**: Seamless transition to contact form upon successful login

### Contact Management
- **Contact Form**: Add contacts with name, address, and mobile number
- **Mobile Number Validation**: Validates Bangladesh mobile number formats (017, 018, 019, 016, 015, 013, 014)
- **Duplicate Prevention**: Prevents adding contacts with existing mobile numbers
- **Logout Functionality**: Secure logout returning to login screen

### User Experience
- **Real-time Validation**: Instant feedback on input fields
- **Visual Feedback**: Color-coded success and error messages
- **Smooth Transitions**: Animated scene transitions between views
- **Responsive UI**: Clean and modern JavaFX interface

## 🛠 Technologies Used

- **Java 21**: Modern Java features and performance improvements
- **JavaFX 21**: Rich desktop application framework
- **JavaFX FXML**: Declarative UI design
- **Maven**: Dependency management and build automation
- **Java Logging API**: Comprehensive error logging

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.6+** (or use the included Maven Wrapper)
- **JavaFX 21** (included via Maven dependencies)

### Verifying Installation

```bash
java -version    # Should show version 21 or higher
mvn -version     # Should show Maven 3.6 or higher
```

## 🚀 Installation

1. **Clone the repository** (or download the project):
   ```bash
   git clone <repository-url>
   cd demo
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```
   
   Or using Maven Wrapper:
   ```bash
   ./mvnw clean compile    # On Unix/Mac
   mvnw.cmd clean compile  # On Windows
   ```

3. **Verify installation**:
   The project should compile without errors.

## 💻 Usage

### Running the Application

#### Using Maven:
```bash
mvn javafx:run
```

#### Using Maven Wrapper:
```bash
./mvnw javafx:run    # On Unix/Mac
mvnw.cmd javafx:run  # On Windows
```

#### Running from IDE:
1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Navigate to `MainApplication.java`
3. Run the `main` method

### Application Workflow

1. **Startup**: The application launches with the login screen
2. **Registration** (New Users):
   - Click "Switch to Registration" on the login screen
   - Fill in name, email, password, and confirm password
   - Click "Register" to create an account
   - Automatically redirected to login screen after successful registration
3. **Login** (Existing Users):
   - Enter registered email and password
   - Click "Login" to authenticate
   - Upon successful login, redirected to contact form
4. **Contact Management**:
   - Enter contact name, address (optional), and mobile number
   - Click "Submit" to save the contact
   - Click "Logout" to return to login screen

## 📁 Project Structure

```
demo/
├── src/
│   └── main/
│       ├── java/
│       │   ├── com/example/demo/
│       │   │   ├── MainApplication.java      # Main entry point and scene management
│       │   │   ├── LoginController.java      # Login logic and validation
│       │   │   ├── RegistrationController.java  # Registration logic and validation
│       │   │   └── ContactFormController.java   # Contact form logic and validation
│       │   └── module-info.java              # Java module configuration
│       └── resources/
│           └── com/example/demo/
│               ├── login-view.fxml           # Login UI layout
│               ├── registration-view.fxml    # Registration UI layout
│               └── contact-form-view.fxml    # Contact form UI layout
├── pom.xml                                   # Maven configuration
├── mvnw                                     # Maven Wrapper (Unix/Mac)
├── mvnw.cmd                                 # Maven Wrapper (Windows)
├── users.txt                                # User data storage (auto-generated)
└── contacts.txt                             # Contact data storage (auto-generated)
```

## 🔄 Application Flow

```
┌─────────────┐
│   Startup   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Login View  │◄───────────────────────────────┐
└──────┬──────┘                                │
       │                                       │
       ├──────────────────┐                    │
       │                  │                    │
       │                  │ After Successful   │
       │                  │ Login              │
       │                  │                    │
       ▼                  ▼                    │
┌─────────────┐  ┌─────────────┐               │
│Registration │  │Contact Form │               │
│   View      │  │    View     │               │
└──────┬──────┘  └──────┬──────┘               │
       │                │                      │
       │                │ Logout               │
       │                │                      │
       │                │                      │
       ▼                │                      │
┌─────────────┐         │                      │
│ Login View  │─────────┴──────────────────────┘
└─────────────┘
```

## ✅ Validation Rules

### Email Validation
- Must follow standard email format: `user@domain.com`
- Validates using regex pattern: `^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$`
- Case-insensitive email matching

### Password Validation
- Password field must not be empty
- Confirmation password must match the original password
- Passwords are stored in plain text (consider encryption for production use)

### Mobile Number Validation
- Must be exactly 11 digits
- Must start with one of the following prefixes:
  - 017, 018, 019, 016, 015, 013, 014
- Validates Bangladesh mobile number format

### General Validation
- All required fields must be filled before submission
- Real-time validation provides immediate feedback
- Submit buttons are disabled until all validations pass

## 💾 Data Storage

The application uses file-based storage for simplicity:

- **`users.txt`**: Stores user registration data
  - Format: `name,email,password` (one per line)
  - Created automatically on first registration
  
- **`contacts.txt`**: Stores contact information
  - Format: `name,address,mobile` (one per line)
  - Created automatically on first contact submission

### Data File Location
Data files are created in the project root directory where the application is executed.

> **Note**: For production use, consider implementing:
> - Database storage (MySQL, PostgreSQL, etc.)
> - Password encryption/hashing
> - Secure data handling practices

## 🔨 Building and Running

### Build the Project
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Create Executable JAR
```bash
mvn clean package
# JAR will be created in target/ directory
```

### Run with JavaFX
```bash
mvn javafx:run
```
