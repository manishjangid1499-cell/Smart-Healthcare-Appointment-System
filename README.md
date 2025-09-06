# 🏥 Smart Healthcare Appointment System (Console, Java + MySQL)

A **console-based healthcare management system** that allows patients to register, book appointments, and manage schedules with doctors.  
Built with **Java 17, JDBC, and MySQL** — designed as a learning project with scope for future enhancements.

---

## ⚙️ Tech Stack
- ☕ Java 17  
- 📦 Maven  
- 🗄️ MySQL  
- 🔗 JDBC  

---

## 🚀 How to Run (Windows)
1. Install:
   - **JDK 17+**
   - **MySQL Server**
   - **MySQL Workbench**
2. In MySQL Workbench, run:
   ```sql
   src/main/resources/database/schema.sql
✨ Features

👤 Patient Management – Register/Login (passwords stored as SHA-256 hash).

🧑‍⚕️ Doctor Management – Add/list doctors.

📅 Appointments – Book, cancel, and view appointments (patients ↔ doctors).

🖥️ Console Menu – Simple text-based navigation.

📌 Notes

🔰 This is a learning starter project.

🔒 Improve validation and error handling in future iterations.

🎨 Future scope: Add GUI (Swing/JavaFX) or build a REST API for web/mobile integration.
