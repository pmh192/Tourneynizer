# Android
This is the Android front end for the application.

## How to Set Up the App with Android Studio

1. Pull the latest version of the source code from the master branch
2. Download Android Studio and open it up
3. Click on open existing project
4. Navigate to the android folder in the git repository and choose it
5. Wait for android studio to sync with the project 
6. Download any tools that are needed to fully sync with the project (An automatic prompt should pop up)
8. Now you have access to the android project

## How to Run the App

1. Click the run button (Looks like a "play" icon)
2. Choose either a device that you connected to your computer or an emulator to run the app on
3. Wait for the project to compile the code and install it on your device
4. Open up your device and interact with the application

## How to run the tests for the Design Patterns

1. Locate the package ```com.tourneynizer.tourneynizer.util (AndroidTest)```
2. Right click on the package and select Run Tests
3. Choose a device or emulator to run the tests on
4. View the results in the console that pops up at the bottom (Should show that they all passed)!

## Known issues to take into account

* As of right now, Match generation for pool type matches don't work. To test matches, please use a bracket type match.
* When creating a user, a session id is not returned to the front end. This causes tournament creation to not work since the user is not technically logged in. To fix this, log out after creating an account and log back in.
