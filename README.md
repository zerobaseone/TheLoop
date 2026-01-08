# CS360
Project for CS-360 - Mobile Architecture and Programming

**App Summary**

The goal of my app was to provide users with a way to track Chicago-based live music events. Many existing event-tracking tools are overly complex or not specialized for the Chicago scene, so my focus was on building something simple and accessible. 

**Screens and Features**

The app includes several screens:

- Create Account - Allows users to register with a username and password
- Main feed/Home - Displays upcoming events.
- History (in development) - Will display past events.
- New Event - The primary feature where users can add and save new events.
- Notifications - For reminders/alerts to help users stay on top of their events.
- User/Profile - Contains account settings and user details.


**Development Approach**

I approached coding the app by breaking the project into smaller, manageable pieces. I first ensured the navigation worked between activities, then added features screen by screen, finally incorporating SMS notifications and the SQLite database. I considered object-oriented programming practices to make the code clean and modular.

**Testing**

To ensure the app was functional, I tested each feature directly in the Android Studio emulator. This included verifying navigation between screens and making sure buttons were linked to the proper activity, and checking that user inputs saved properly. 

**Overcoming Challenges**

One challenge I encountered was managing persistent data. Initially, I used SQLite for local storage to save events. However, I realized that if the app were to scale or include features like friending and sharing events, I would need another cloud-based solution (such as Firebase or to build my own server with a relational database). This forced me to think beyond the class project and consider how apps scale in professional development.

**Strengths and Successes**

I was particularly successful in implementing the SQLite databases for local event storage. This gave me hands-on experience with database integration, queries, and managing persistent user data. Additionally, I feel I succeeded in creating a clean UI that could serve as a foundation for future iterations of the app.

**Future Improvements**

To make the best app possible, there are several things I'd like to accomplish in the future:

- Security: User details are better encrypted, or moved to a Google SSO option, input validation to ensure protection against SQL injection
- Cloud Database Integration: Move from local SQLite storage to Firebase or a server-hosted database to allow syncing across devices.
- Friending and Sharing: Add functionality for users to connect with friends to see who's going and share events.
- Push Notifications: Expand the notification preferences.
- Cross-Platform Support: Explore iOS development.
- UI Enhancements: Improve the design with an app-specific visual language and color palette, including custom logo 
