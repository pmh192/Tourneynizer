* `public/index.html` is the page template;
* `src/index.js` is the JavaScript entry point.


## Instructions for running
* Localhost is on port 3000 by default. To change ports, edit the start script under 'package.json' change add PORT='YOURPORT' to the start script so it reads "start":"PORT=YOURPORT react-scripts start"
* You should first clone the stable version of the project from master
* To run this project, you must have node installed go to: https://nodejs.org/en/download/
  * Download the version appropriate for your OS
  * You must have at least v 6.12.3 of node to run correctly
* Now that you have node installed, you can easily install react
  * from the command line install react v 16.2.0 using 'npm install --save react'
* cd into Tourneynizer/website/
* now you may run the app with command 'npm start' which will open up the web app in the default browser
   * This will open the react app on localhost 3000 and you may interact with the app
   * NOTE: as of the most recent version, log in does not persist past page refresh
   * Currently you can:
      * Create an Account
      * Log in to your Account
      * View all tournaments on the server
      * Stay logged into your account when switching pages (without refreshing)

## ScreenShots
![Alt text](./screenshots/AccountCreationPage.png?raw=true "AccountCreationPage")
![Alt text](./screenshots/HomePage.png?raw=true "HomePage")
![Alt text](./screenshots/Loginpage.png?raw=true "LoginPage")
![Alt text](./screenshots/TournamentViewPage.png?raw=true "TournamentViewPage")
