# 📂 Document360 API Task – Folder Operations (GET, POST, PUT, DELETE)

## 🚀 Overview

This project demonstrates how to perform CRUD operations (Create, Read, Update, Delete) on **Document360’s Drive Folders API** using Java.

You’ll find code to:
-  Fetch all folders (GET)
-  Create a new folder (POST)
-  Rename a folder (PUT)
-  Delete a folder (DELETE)

---

##  Language & Tools Used

- **Language**: Java 17
- **Tools**:
- Java
- Native `HttpURLConnection`
- JSON handling with basic string ops (no external libraries)

---

## 🧪 Steps to Run the Project

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/your-username/document360-java-api-task.git
   cd document360-java-api-task

2. **configure your api-token**  

   Add a config.properties file inside src/main/resources/:

   api.token=your_api_token_here
   user.id=your_user_id_here
   base.url=https://apihub.document360.io/v2/Drive/Folders

3. **compile and run the assesment.java** 
   
   -javac assesment.java
   
   -java assesment.java
