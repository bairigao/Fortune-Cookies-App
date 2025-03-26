# Digital Fortune Cookies for Students

## Project Brief

**Project Title:** Digital Fortune Cookies for Students  
**Synopsis:**  
This project aims to develop a fun and interactive application designed to provide students with personalized study tips, motivational quotes, and fun predictions. Leveraging AI-powered natural language understanding and generation, the tool will analyze user input (e.g., study habits, preferences, and progress) and deliver tailored messages to make studying more enjoyable and engaging. The application will include a graphical user interface (GUI) based on JavaFX and core features such as authentication, persistence, and progress tracking.

**Target Audience:**  
Students seeking a lighthearted, motivating, and personalized tool to enhance their study routines and maintain engagement.

**Objective:**  
To create an application that combines AI capabilities with motivational elements, delivering personalized content to improve the student learning experience.

---

## Initial Requirements

### 1. Authentication System
- **Sign-up Functionality**  
  - Users can create an account with a username, password, and optional email.  
  - Passwords will be hashed for security.  
- **Sign-in Functionality**  
  - Users can log in with their credentials.  
  - Invalid login attempts will show appropriate error messages.  
- **Persistent User Data**  
  - User data (e.g., credentials and preferences) will be stored in a database.

---

### 2. Personalized Content Features
- **Study Tips**  
  - AI-generated tips based on the student’s preferred subjects, study habits, or past interactions.  
  - Tips categorized into short-term (daily) and long-term (weekly) advice.  

- **Motivational Quotes**  
  - Dynamic quotes tailored to the user's study progress or mood.  
  - The application will offer "refresh" functionality to display a new quote on demand.

- **Fun Predictions**  
  - Random but study-related predictions (e.g., "You will ace your exam if you study for 2 more hours!") to keep users engaged.

---

### 3. Progress Tracking
- **Track Study Progress**  
  - Users can input completed study hours, topics covered, and goals.  
  - The application generates summary statistics and trends (e.g., study streaks).  
- **Tailored Recommendations**  
  - Content recommendations (tips, quotes) adapt to the user’s progress and behavior.  
- **Graphical Representation**  
  - A visual chart (e.g., bar or line chart) to show progress trends over time.

---

### 4. User Interface
- **Main Features Window**  
  - A central dashboard displaying study tips, motivational quotes, fun predictions, and progress charts.  
  - Buttons to navigate between different sections (e.g., "My Progress," "Daily Tip").  
- **Clean and Intuitive Design**  
  - Responsive and visually appealing GUI built with JavaFX.  
  - Use of icons, animations, and color schemes for a motivating aesthetic.  

---

### 5. Persistence System
- **User Data Storage**  
  - Store user credentials, preferences, progress data, and interaction history.  
- **Database Implementation**  
  - A relational database (e.g., SQLite) will be used for simplicity.  

---

### 6. AI-Powered Functionalities
- **Natural Language Processing (NLP)**  
  - AI generates personalized content using pre-trained models.  
  - Integration with a provided OpenAI-compatible API for natural language generation.  
- **Dynamic Content Updates**  
  - Content updates daily or upon specific triggers (e.g., reaching a study milestone).  

---

### 7. Additional Features
- **Notifications System**  
  - Notify users of daily tips, motivational quotes, or missed study goals.  
- **Customizable Settings**  
  - Users can choose themes, preferred motivational styles (e.g., humorous, serious), and update goals.  
- **Offline Functionality**  
  - Allow basic features (e.g., viewing saved progress or offline quotes) to work without internet access.

---

### Deliverables
1. Functional Java application with the features listed above.  
2. Detailed documentation of the code, including UML diagrams for OO design.  
3. User manual for application use.  
4. Presentation showcasing the project features and development process.
