# PositivityShare
Android app where users can send random compliments to random users

INSTALLATION

Download project as a zip file. Unzip files to your computer. Open Android Studio. Choose File/Open from the menu. Navigate to the project directory of this project. Click the app folder. Click Choose. The project should open on your screen.


GETTING STARTED

You will need a Firebase account. After running the INSTALLATION step, make a copy of your SHA1 from Android Studio and copy it into the add fingerprint portion of the Firebase Project Overview settings on the Firebase console.  You will then need to download the google-services.json file and paste it into your PositivityShare/app directory.  Enable Google sign in the Firebase Authentication and you should be all good to go.


API/LIBRARIES

Google Firebase - for database/storage authentication, NoSql database and file Storage.

SQLite OpenHelper - for SQL database

Google Sign-in - for user authentication

Volley - for REST API calls and parsing JSON

ComplimentR - for the compliments JSON API: https://complimentr.com/

Recyclerview/CardView - for scrolling (use of pagination to load Firebase data upon scroll)

Glide - for image loading

Swisyn-Android-Onboarder - for Android onBoarding for the new user screen


MORE INFO

Set up for use in any orientation and use on phones or tablets.  

NEEDED

I still need to set up functionality for sending inspirations to other users.  The button is there, but the functionality is not due to not being able to find a free inspiration API.

AUTHOR

Randy Perrone
