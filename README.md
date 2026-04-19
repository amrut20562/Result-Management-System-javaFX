# 🎓 Student Result Management System (JavaFX)

A comprehensive desktop graphical software for managing student data and publishing academic examination results efficiently. Developed purely with **JavaFX** and **MySQL**, adhering to the **Model-View-Controller (MVC)** architectural pattern.

This application acts as a digital ledger to insert, analyze, search, update, and evaluate examination marks, culminating in highly customized PDF exam result card generation.

---

## 🛠️ Technologies & Terminologies Used

- **Java (JDK 25):** The core object-oriented programming language driving backend and frontend coordination.
- **JavaFX & FXML:** Used to build the modern graphical user interface (GUI). `FXML` is the XML-based language used to define user layouts cleanly separated from Java logic.
- **MVC Pattern (Model-View-Controller):** 
  - **Model:** Classes like `Student.java` and `Subject.java` representing precise data objects.
  - **View:** `*.fxml` files representing the visual presentation. 
  - **Controller:** Logic routers like `StudentController.java` acting as the bridge linking Views to Models and Database events.
- **JDBC (Java Database Connectivity):** The standard Java API executing direct SQL requests. Facilitates communication to the relational database.
- **MySQL:** A relational database management system storing rigid, interconnected tables of students, subjects, and relational aggregate marks natively.
- **iText PDF (v5):** A powerful robust external Java library used to construct memory-optimized `.pdf` document trees for printable PDF "Result Cards".
- **PowerShell (.ps1):** Windows terminal scripting used to batch compile sources globally using standard VM arguments without Maven.

---

## 🏗️ Step-by-Step Implementation & Setup Guide

Since this repository is structurally raw and compiled manually without a dependency manager like Maven, you must configure a runtime environment manually or use the provided `.ps1` auto-run script.

### Step 1: Pre-requisites & Database Emulation
1. Ensure **Java JDK 17+** and **JavaFX SDK** are downloaded onto your machine.
2. Install **MySQL Workbench / Server** locally.
3. Import or create the `be_results_db` database using standard SQL DDL queries matching the internal `Student`, `Subject`, and `Marks` schema.
4. Update `DBConnection.java` internally to represent your local MySQL `USERNAME` and `PASSWORD`.

### Step 2: Grabbing External Dependencies (JARs)
This project requires exactly two open-source libraries placed into your local classpath.
1. Create a `lib/` folder in the root directory.
2. Download [MySQL Connector JDBC](https://dev.mysql.com/downloads/connector/j/) and extract its `.jar`.
3. Download [iText PDF v5](https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13.3/) `.jar`. 
4. Move both downloaded `.jar` files into the `lib/` directory.

### Step 3: Compiling and Execution (Command Line Tool)
We provide a powerful `run.ps1` PowerShell script that intercepts complex JavaFX modules.
1. Open PowerShell and navigate to the project directory.
2. Set the `$JavaFXPath` variable in the `run.ps1` script to perfectly match where your JavaFX SDK `lib` folder exists locally.
3. Run the application logic:
   ```powershell
   .\run.ps1
   ```
*This automatically invokes `javac` over the `src/` modules, builds `.class` binaries into a `bin/` repository, clones the `.css`/`.fxml` structure, and boots `app.Main`!*

### Step 4: IDE Setup (Optional, but Recommended)
If utilizing **Visual Studio Code**, **IntelliJ**, or **Eclipse**:
1. Mount the folder into the IDE.
2. Add the two `lib/` files permanently to your **Referenced Libraries / Build Path**.
3. Supply the JavaFX execution flags securely via VM arguments (`launch.json` is supplied by default for VS Code).
   ```json
   "vmArgs": "--module-path \"C:\\Path\\To\\javafx-sdk\\lib\" --add-modules javafx.controls,javafx.fxml"
   ```
4. Click Compile/Run focusing on `app.Main.java`.

---

## 🚀 Core Application Features
- 👥 **Student Directory:** Comprehensive registry system locking duplicate entries.
- 📝 **Marks Interface:** Intelligent update/insert hooks modifying marks safely for 6 B.E AIDS subjects.
- 🔍 **Search Filtering:** Fuzzy-match student registry querying via standard Name AND/OR Roll Number metrics.
- 📊 **Metric Dashboards:** JavaFX-rendered Pie and Bar metrics parsing cross-student distributions and averages. 
- 📄 **PDF Reporting:** Dynamic, stylized, memory-compiled `.pdf` generation containing subject totals and graded percentile bands perfectly formatted.
