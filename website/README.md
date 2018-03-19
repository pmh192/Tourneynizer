## Pre-Requisites
Have NodeJS and NPM installed. You can download both as a bundle here: https://nodejs.org/en/download/ . Download the version appropriate for your OS. Node v6.12.3 and its associated NPM are the minimum version.

## Setup the App

1. You should first clone the stable version of the project from master.
2. Run `cd website/` inside the cloned directory.
3. Run `npm install`. This will install all dependencies for the website.

## Instructions for running

1. Now you may run the app with command `npm start`. This will start the server on port 3000. If you need to change the start port, run `PORT=[myport] npm start`.
2. The website should automatically open in your default web browser. If it doesn't, open your browser and navigate to `http://localhost:3000` or whatever port you started the server on.

## Current Issues
* Logout sometimes does not work correctly

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
