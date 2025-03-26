# Crack On Cookies

**Organization GUI** built for time management and personal efficiency, offering personalized support and encouragement delivered by an integrated AI system.

---

## Minimum Requirements

### Login System
- Functioning GUI.
- Create Account functionality.
- User data storage.
- Account access and management.

### Data Storage
- Secure storage of user data.

### Calendar System
- Functioning GUI for calendar management.
  - Options:
    - Use a third-party library (e.g., JCalendar).
    - Build the calendar from scratch (check with the teaching team).  
- Calendar inputs linked to user data:
  - Clicking a date allows the user to write a title (e.g., for AI content generation).
  - Store additional information tied to specific dates.
  - If no data is provided, default to sending motivational messages.

### Integrated AI System
- Sends daily efficiency and motivational messages.
  - Minimum requirement: Send one message per login.
  - Eliminate repeated messages or topics.

---

## Maximum Requirements

### Login System
- Functioning GUI.
- Create Account functionality.
- User data storage.
- Account access and management.

### Data Storage
- Enhanced storage functionality.

### Calendar System
- Functioning GUI for calendar management.
  - Options:
    - Use a third-party library (e.g., JCalendar).
    - Build the calendar from scratch (check with the teaching team).  
- Calendar inputs linked to user data:
  - Clicking a date allows the user to write a title and additional information.
  - AI generates messages based on user-provided data.
  - **Enhanced functionality**:
    - Mark priority or importance of specific dates/activities.
    - Add weekly repeatable activities/study schedules.
    - Exclude specific dates from AI-generated messages.

### Integrated AI System
- GUI for messages:
  - Popup windows or integration with the calendar interface.
- Sends daily efficiency and motivational messages:
  - Limit to one message per day, regardless of multiple logins.
  - Allow users to request additional messages.
  - Expand responses based on calendar data:
    - Avoid repeated messages to reduce pressure on users.
    - Allow repeated messages for upcoming important dates.
    - Combine messages with study schedules or deadlines:
      - Example: "Time to crack on with some CAB203 study today. Don’t forget your due date on Friday 28th for MXB101."
    - Provide generic motivational messages on less important days.
