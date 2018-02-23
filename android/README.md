# How to Set Up the App with Android Studio

1. Pull the latest version of the source code from the master branch
2. Download Android Studio and open it up
3. Click on open existing project
4. Navigate to the android folder in the git repository and choose it
5. Wait for android studio to sync with the project 
6. Download any tools that are needed to fully sync with the project (An automatic prompt should pop up)
8. Now you have access to the android project

# How to Run the App

1. Click the run button (Looks like a "play" icon)
2. Choose either a device that you connected to your computer or an emulator to run the app on
3. Wait for the project to compile the code and install it on your device
4. Open up your device and interact with the application

# How to run the tests for the Design Patterns

1. Locate the package ```com.tourneynizer.tourneynizer.util (AndroidTest)```
2. Right click on the package and select Run Tests
3. Choose a device or emulator to run the tests on
4. View the results in the console that pops up at the bottom (Should show that they all passed)!

# Known issues to take into account

* As of right now the TournamentType enums are not synced up acroos the front end and back end.
This results in tournament creation returning an error if you try to select a Tournament Type other than the first.
To fix this bug, we will first sync our enums.
However, for more modularity we will later expose the enum on the back end and use that on the front end, thus removing the issue.
* When creating a user, a session id is not returned to the front end. This causes tournament creation to not work since the user is not technically logged in.
To get around this issue, we will do email verification on the back end and not allow a user to access the application after creating an account on the front end.
* Since we have not gotten around to sorting data on the back end, newly created tournaments are in the bottom of the tournament list.
So after creating a tournament and refreshing the list of tournaments, scroll to the bottom of the list to see your new tournament.