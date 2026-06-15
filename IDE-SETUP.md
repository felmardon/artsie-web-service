# IDE Setup Guide

This project is configured to open seamlessly in Eclipse, VS Code, IntelliJ IDEA, Netbeans, and other IDEs that support Maven projects.

## Prerequisites

- **Java 17** or higher installed
- **Maven 3.6.0** or higher installed
- Git

## Quick Start - Choose Your IDE

### 1. **Eclipse IDE** (Most straightforward)

#### Option A: Import Existing Project
1. Open Eclipse
2. Go to **File → Import → General → Existing Projects into Workspace**
3. Browse to the project root directory (`artsie-web-service`)
4. Click **Finish**
5. Wait for Eclipse to download dependencies and build the project
6. The project has pre-configured `.classpath`, `.project`, and `.settings/` files

#### Option B: Clone and Import
```bash
git clone <repository-url>
cd artsie-web-service
```

### 2. **VS Code**

#### Setup Steps
1. Open the project folder in VS Code: `code artsie-web-service`
2. Install recommended extensions when prompted (click "Install All" in the extensions dialog)
   - Red Hat Java Extension Pack
   - Maven for Java
   - Spring Boot Dashboard
   - And others...

3. Wait for the Java Language Server to initialize (check Output panel)
4. Build the project: Open Command Palette (Cmd+Shift+P) → **Maven: Build**

#### Running/Debugging
- Press **F5** to start debugging with the pre-configured launch configuration
- Use the Maven explorer on the left sidebar to run Maven tasks

### 3. **IntelliJ IDEA (Community or Ultimate)**

1. Open IntelliJ IDEA
2. Go to **File → Open** and select the project root directory
3. IntelliJ will automatically detect it as a Maven project
4. Click **Trust Project** if prompted
5. Wait for Maven to sync dependencies
6. Mark `src/main/java` as Sources Root if not done automatically

#### Running the Application
- Right-click on `com.felmardon.artsie.ArtsieApplication` → **Run** or **Debug**
- Or use the Run menu and select your configuration

### 4. **Netbeans**

1. Open Netbeans
2. Go to **File → Open Project**
3. Navigate to the project root and select it
4. Netbeans will automatically detect and open it as a Maven project
5. The project will load and resolve dependencies

## Project Structure

```
artsie-web-service/
├── artsie-domain/        # Domain entities and repositories
├── artsie-storage/       # S3 and storage services
├── artsie-api/          # REST API controllers and DTOs
├── artsie-admin/        # Admin API endpoints
├── artsie-app/          # Main Spring Boot application
├── pom.xml              # Maven parent POM
└── artsie-*             # Module POMs
```

## Java Version

The project is configured for **Java 17**. Ensure your IDE is configured to use Java 17 or later.

## Configuration Files

This repository includes IDE-specific configuration files:

- **`.classpath`, `.project`, `.settings/`** - Eclipse configuration
- **`.vscode/`** - VS Code configuration including:
  - `settings.json` - Language Server settings
  - `extensions.json` - Recommended extensions
  - `launch.json` - Debug configurations
  - `tasks.json` - Build tasks
- **`.editorconfig`** - Code style consistency across all IDEs
- **`.gitignore`** - Properly configured for all IDEs and Maven builds

## Building the Project

### Using Maven (command line)
```bash
# Clean and build all modules
mvn clean package

# Run tests
mvn test

# Build specific module
mvn -pl artsie-app clean package

# Run the application
mvn spring-boot:run -pl artsie-app
```

### Using IDE Tools
- **Eclipse**: Right-click project → Run As → Maven build
- **VS Code**: Command Palette → Maven: Build
- **IntelliJ**: Click the Maven tool window on the right sidebar

## Troubleshooting

### Java Version Issues
If you see compilation errors related to Java versions:
1. Ensure Java 17+ is installed
2. In **Eclipse**: Project → Properties → Java Compiler → Check JDK Compliance level
3. In **VS Code**: Open `.vscode/settings.json` and set `java.home` to your Java 17 installation
4. In **IntelliJ**: Go to Project Structure → Project → SDK and select Java 17+

### Maven Resolution Issues
- **Eclipse**: Right-click project → Maven → Update Project (Alt+F5)
- **VS Code**: Run Maven: Reload Maven Projects
- **IntelliJ**: Reload Maven projects in the Maven tool window

### Dependencies Not Downloaded
```bash
# Force re-download of all dependencies
mvn clean dependency:resolve
```

### Slow Performance in VS Code
Update `.vscode/settings.json` with appropriate VM arguments:
```json
"java.jdt.ls.vmargs": "-XX:+UseParallelGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -Dsun.zip.disableMemoryMapping=true -Xmx2G -Xms100m"
```

## Code Style

The project uses EditorConfig to maintain consistent code formatting across all IDEs. Most IDEs will automatically respect the `.editorconfig` file:

- Java files: 4-space indentation, max line length 120
- XML files: 4-space indentation
- YAML files: 2-space indentation
- JSON files: 2-space indentation

## Switching Between IDEs

You can safely switch between IDEs:
1. The `.classpath`, `.project`, and `.idea/` folders are IDE-specific
2. The `pom.xml` and module POMs are the source of truth for the build
3. Just open/import the project in your new IDE - it will regenerate IDE-specific files as needed

## Contributing

Before committing code:
1. Ensure the project builds: `mvn clean package`
2. Run tests: `mvn test`
3. Your IDE should automatically format code according to `.editorconfig`

## Additional Resources

- [Maven Documentation](https://maven.apache.org/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Eclipse User Guide](https://www.eclipse.org/eclipse/platform-core/documents/user_guide/eclipseRunner_commandline.html)
- [VS Code Java Documentation](https://code.visualstudio.com/docs/languages/java)
- [EditorConfig Documentation](https://editorconfig.org/)

