

# 🧠 CPU Scheduling Algorithm Simulator
by Thy Sethasarakvath && Do Davin

---

## 📚 Table of Contents
  - [🎯 Objectives](#-objectives)
  - [🛠️ Technology Stack](#️-technology-stack)
  - [📁 Project Structure Overview](#-project-structure-overview)
  - [⚙️ Implemented Algorithms](#️-implemented-algorithms)
  - [📥 Process Input](#-process-input)
  - [📊 Simulation Output](#-simulation-output)
  - [🧪 Challenges \& Design Decisions](#-challenges--design-decisions)
  - [🛠️ How to Fix Library Errors After Cloning](#️-how-to-fix-library-errors-after-cloning)
    - [✅ 1. Verify `settings.json`](#-1-verify-settingsjson)
    - [✅ 2. Fix `launch.json` Configuration](#-2-fix-launchjson-configuration)
      - [🔧 Original `launch.json`:](#-original-launchjson)
      - [🔧 After cloning:](#-after-cloning)
      - [📝 Steps to Fix:](#-steps-to-fix)
    - [✅ 3. Check Java Runtime Version](#-3-check-java-runtime-version)
    - [🧪 Final Tip](#-final-tip)

---

## 🧠 Project Title
**CPU Scheduling Algorithm Simulator**

---

## 🎯 Objectives
- Demonstrate how CPU scheduling algorithms operate within an OS.
- Implement and simulate five key scheduling strategies: FCFS, SJF, SRT, RR, and MLFQ.
- Visualize process execution using a Gantt chart.
- Calculate and compare key performance metrics: Waiting Time, Turnaround Time, Response Time.

---

## 🛠️ Technology Stack
- **Language:** Java  
- **UI:** JavaFX (FXML-based interface)  
- **Visualization:** Custom Gantt chart rendering via JavaFX Canvas or layout components  
- **Automation & Build:** Bash scripting (optional), `.vscode` config for reproducibility  

---

## 📁 Project Structure Overview
```plaintext
CPU-Scheduling/
├── .vscode/              # VSCode launch and settings config
├── bin/                  # Compiled class files
├── lib/                  # External libraries
├── src/
│   ├── FXML/             # JavaFX layout file (main.fxml)
│   ├── Image/            # UI assets (e.g., itc.png)
│   ├── Controller.java   # Handles UI logic and user interaction
│   ├── GanttChart.java   # Renders Gantt chart visualization
│   ├── GanttEntry.java   # Represents a segment in the Gantt chart
│   ├── Main.java         # Application entry point
│   ├── Process.java      # Core data structure for scheduling
│   └── style.css         # UI styling
└── README.md             # Setup, usage, and documentation
```

---

## ⚙️ Implemented Algorithms

| Algorithm | Type                     | Preemptive | Notes                                      |
|----------|--------------------------|------------|--------------------------------------------|
| FCFS     | Basic Queue              | No         | Processes executed in arrival order        |
| SJF      | Shortest Job             | No         | Chooses shortest burst time                |
| SRT      | Shortest Remaining Time  | Yes        | Dynamic selection based on remaining time  |
| RR       | Round Robin              | Yes        | Time quantum configurable                  |
| MLFQ     | Multilevel Feedback Queue| Yes        | 3 queues with aging, promotion/demotion    |

---

## 📥 Process Input
- **Source:** GUI form (via JavaFX), CLI (optional)  
- **Fields:** Arrival Time, Burst Time, (Priority for MLFQ)  
- **Sample Input:**
  ```plaintext
  Arrival Time:
  0 1 2 3

  Burst Time: 
  2 4 6 8

  Quantum (RR or MLFQ):
  2 4
  ```

---

## 📊 Simulation Output
- **Gantt Chart:** Dynamically rendered using JavaFX  
- **Metrics Table:**
  - Waiting Time (Turnaround Time - Burst Time)
  - Turnaround Time (Finish Time - Arrival Time)
  - Response Time (Start Time - Arrival Time)
  - Averages for each metric  
- **Comparison:** Side-by-side performance analysis across algorithms  

---

## 🧪 Challenges & Design Decisions
- **JavaFX Resource Loading:** Ensured reproducibility across platforms using relative paths and proper packaging.  
- **Modular Architecture:** Each algorithm is encapsulated for easy testing and extension.  
- **UI/UX Polish:** Styled with CSS for clarity and accessibility; ComboBoxes and cards refined for intuitive interaction.  
- **Reproducibility:** `.vscode` launch configs and clear folder structure support team collaboration and instructor review.  

---

## 🛠️ How to Fix Library Errors After Cloning

When cloning the CPU Scheduling Simulator project into VS Code, you may encounter library or launch configuration errors. Follow these steps to ensure your environment is correctly set up.

---

### ✅ 1. Verify `settings.json`

Ensure your `.vscode/settings.json` file contains the correct project structure and library references:

```json
{
  "java.project.sourcePaths": ["src"],
  "java.project.outputPath": "bin",
  "java.project.referencedLibraries": {
    "include": ["lib/**/*.jar"]
  }
}
```

---

### ✅ 2. Fix `launch.json` Configuration

VS Code may auto-generate a new project name in `launch.json`. This can cause mismatches with your original configuration.

#### 🔧 Original `launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Main",
      "request": "launch",
      "mainClass": "Main",
      "projectName": "cpu-scheduling_705f341a",
      "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
    }
  ]
}
```

#### 🔧 After cloning:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Main",
      "request": "launch",
      "mainClass": "Main",
      "projectName": "cpu-scheduling_409287b",
      "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
    },
    {
      "type": "java",
      "name": "Main",
      "request": "launch",
      "mainClass": "Main",
      "projectName": "cpu-scheduling_705f341a",
      "vmArgs": "--module-path lib/libs --add-modules javafx.controls,javafx.fxml,javafx.graphics"
    }
  ]
}
```

#### 📝 Steps to Fix:
1. Copy the new project name (e.g., `cpu-scheduling_409287b`) from the first configuration.  
2. Paste it into the second configuration, replacing the old name.  
3. Comment out or delete the first configuration.  
4. Save the file.  

---

### ✅ 3. Check Java Runtime Version

Ensure you're using the correct JDK version for JavaFX compatibility:

- Open the command palette: `Ctrl+Shift+P` → search for **Java: Configure Java Runtime**
- Confirm the runtime is set to:  
  `21.0.8-win32-x86_64`

---

### 🧪 Final Tip

If you're still encountering issues:
- Clean and rebuild the project  
- Restart VS Code  
- Ensure all `.jar` files are present in the `lib/` directory  

---
