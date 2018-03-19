# Web
This is website front end for the application.

## Pre-Requisites
Have NodeJS and NPM installed. You can download both as a bundle here: https://nodejs.org/en/download/ . Download the version appropriate for your OS. Node v6.12.3 and its associated NPM are the minimum version.

## Setup the App

1. You should first clone the stable version of the project from master.
2. Run `cd website/` inside the cloned directory.
3. Run `npm install`. This will install all dependencies for the website.

## Instructions for running

1. Now you may run the app with command `npm start`. This will start the server on port 3000. If you need to change the start port, run `PORT=[myport] npm start`.
2. The website should automatically open in your default web browser. If it doesn't, open your browser and navigate to `http://localhost:3000` or whatever port you started the server on.

## Trouble running the app?
Try the following:
1. npm install -g npm@latest (don't worry if this fails its to update npm)
2. rm -rf node_modules (to remove the existing modules.)
3. npm install (to re-install the project dependencies.)
4. development was done on google chrome. Not all features might be supported on other browsers. Please use Google chrome

## Current Issues
* Logout sometimes does not work correctly
  * Logout will redirect to the login page and you can still change accounts. If you want to create a new account close the terminal and run npm start again OR clear your cookies. The problem stems from cookies not being destroyed correctly on logout api call.

## Important Files
* `public/index.html` is the page template
* `src/index.js` is the JavaScript entry point.

## ScreenShots
![Alt text](./screenshots/AccountCreationPage.png?raw=true "AccountCreationPage")
![Alt text](./screenshots/HomePage.png?raw=true "HomePage")
![Alt text](./screenshots/Loginpage.png?raw=true "LoginPage")
![Alt text](./screenshots/newtournamentview.png?raw=true "New Tournament View")
![Alt text](./screenshots/Googlemaps.png?raw=true "Google Maps")
![Alt text](./screenshots/CreateTournament.png?raw=true "Create a Tournament")
