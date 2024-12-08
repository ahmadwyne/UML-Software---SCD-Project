# UML-Software---SCD-Project

## Introduction

The **UML Editor** project is designed to facilitate software design activities, particularly focusing on the management and creation of UML diagrams such as class diagrams, sequence diagrams, and use case diagrams. The primary goal of the system is to provide a user-friendly and flexible UML editing tool that aids in creating relevant design artifacts for a software project. Additionally, the system should generate initial code stubs for various components based on UML models.

### Key Features:
- **Create and manage UML diagrams**: Create, save, and load various UML diagrams (e.g., Class, Use Case, Sequence).
- **Design View**: Visual interface to depict and interact with UML diagrams.
- **Code Generation**: Generate code stubs based on UML diagrams in supported programming languages.

## Main Requirements

### 1. Create and Manage a Project
- **1.1** Organize different artifacts (e.g., class diagram, use-case diagram) within a project.
- **1.2** Maintain model information, including components, constraints, and relationships (e.g., Class, Interface, Package, Actor, Usecase, Association, Inheritance, etc.).
- **1.3** Implement functionality to save and load project information.

### 2. Provide a Design View for Depiction of UML Diagrams
- **2.1** Support visual forms/shapes for creating UML diagrams.
- **2.2** Manage positioning and layout of diagram components (e.g., shapes).
- **2.3** Export diagrams to **PNG** / **JPEG** formats.

### 3. Generate Code in Supported Programming Languages
- **3.1** Translate model components (e.g., classes, interfaces) into corresponding code files in the chosen programming language.
- **3.2** Handle relationships and constraints (e.g., inheritance, association, multiplicity) during code generation.

## Use Case Diagram

The use case diagram provides an overview of the system’s functionality, showing the main user interactions and processes. The system is expected to handle:
- **User Login and Authentication**.
- **Project Creation and Management**.
- **Diagram Creation and Editing**.
- **Code Generation**.

The diagram will be further detailed based on the developers' needs and the system's functional requirements.

## Class Diagram

The class diagram outlines the conceptual structure of the system, focusing on the key abstractions, their relationships, and responsibilities:
- **Project**: Responsible for organizing artifacts and managing the overall project.
- **Model**: Handles the creation and management of UML diagrams.
- **Component**: Represents individual elements like classes, interfaces, actors, and use cases.
- **Diagram**: Represents different types of UML diagrams (e.g., Class Diagram, Use Case Diagram).
- **CodeGenerator**: Translates UML models into corresponding code in the target language.

This conceptual class diagram should be used as a starting point, and it can be refined further during the development phase.

## Constraints

The following constraints were adhered to during the development process:

1. **Layered Architecture**: Implement a layered architecture with clear separation of UI, Business, and Data layers to ensure a maintainable and scalable system.
2. **Design Patterns**: Use relevant design patterns to improve the quality and maintainability of the codebase.
3. **Exception Handling**: Implement comprehensive exception handling and logging to ensure robustness.
4. **Test-Driven Development (TDD)**: Followed a test-driven approach by writing unit tests for all components and ensuring adequate test coverage.
5. **Documentation**: Documented classes, methods, and other critical components using JavaDoc comments, ensuring clarity and maintainability.
6. **Version Control**: Commit code to a Git repository regularly, ensuring proper version control practices are followed. The repository should be hosted on GitHub.

## Technologies Used

- **Java**: Core programming language.
- **JavaFX**: For building the graphical user interface (GUI) to display and interact with UML diagrams.
- **Maven**: Project management and build automation tool.
- **JUnit**: For unit testing.
- **Git/GitHub**: For version control and project collaboration.
- **JDK 21**: Java Development Kit for compiling and running the project.

## Installation Instructions

To get started with the UML Editor project, follow these steps:

### Prerequisites:
- **JDK 11+** installed on your machine.
- **Maven** for building the project.
- **Git** for version control.
- An IDE like **IntelliJ IDEA** to work with JavaFX and Maven-based projects.

### Clone the Repository:
1. Clone the project from GitHub:
   git clone https://github.com/ahmadwyne/UML-Software---SCD-Project.git   

### Build and Run the Project:

1. **Build the Project** using Maven:
   mvn clean install

2. **Run the Application**:
   mvn javafx:run

### Running Tests:
Run the unit tests using Maven to ensure everything is functioning correctly:

mvn test

## Contributions

Feel free to fork this repository and contribute by submitting pull requests. All contributions are welcome!

### Guidelines:
- Follow **code style guidelines**.
- Ensure your code is well-documented with **JavaDoc comments**.
- Provide **unit tests** for new features or bug fixes.
