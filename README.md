# 🚀 Hierarchical Task Management System

A robust Java Desktop application designed for corporate environment workflow management. It features a sophisticated task-employee mapping system using a hierarchical structure (Composite Pattern) and automated productivity tracking.

---

## 📌 Core Features

### 👥 Human Resources Management
* **Employee Registration**: Add employees with unique IDs and names, featuring RegEx validation to ensure data integrity.
* **Performance Tracking**: Automatically calculates total work duration based exclusively on "Completed" tasks.

### 📝 Task Architecture (Composite Design Pattern)
* **Simple Tasks**: Individual units of work with specific start/end hours.
* **Complex Tasks**: Containers that can nest multiple sub-tasks (Simple or Complex), allowing for an infinite tree-like hierarchy.
* **Dynamic Status Updates**: Interactive dashboard allowing users to toggle task completion status directly from the UI.

### 📊 Reporting & Analytics
* **Best Employees Report**: Generates a sorted list of employees who exceeded 40 completed work hours.
* **Status Distribution**: Real-time visualization of the ratio between Completed and Uncompleted tasks per employee.

---

## 🛠️ Technical Implementation & OOP Concepts

The project demonstrates advanced Java programming principles and Design Patterns:

* **Composite Pattern**: Used to treat individual tasks and compositions of tasks uniformly through the `Task` abstract class.
* **Polymorphism**: The `estimateDuration()` method is called on the generic `Task` type but executes specific logic depending on the concrete instance (`Simple` or `Complex`).
* **Recursion**: Heavily utilized for deep-tree searching, status updates across hierarchies, and visual rendering in the `JTable`.
* **Binary Serialization**: State persistence is handled via `ObjectOutputStream`, saving all data to `DataFile.txt` upon application exit.
* **Data Structures**: Leveraging `Map<Employee, List<Task>>` for efficient O(1) or O(n) data retrieval and grouping.



---

## 🖥️ User Interface (GUI)

The application uses a `JTabbedPane` layout for a clean user experience:
1. **Management Tab**: Dedicated to employee registration, task creation, and assignment.
2. **Dashboard Tab**: An interactive tree-table representation of the workforce and their respective task hierarchies.
3. **Reports Tab**: A diagnostic area for viewing productivity metrics and status summaries.

---

## ⚙️ Requirements & Running

1. **Java JDK 17+**: Required for `sealed` class support and modern Java features.
2. **IDE**: Optimized for **IntelliJ IDEA** to support `.form` GUI files.
3. **Execution**: Run `MainFrame.java` located in the `graphical_user_interface` package.

---

**Author:** Ilban Daniel

**Project Type:** Academic / Object-Oriented Programming 
