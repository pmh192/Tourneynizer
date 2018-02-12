import React, { Component } from 'react';
import HomePage from './PageComponents/HomePage';
import LoginPage from './PageComponents/LoginPage';
import AccountCreationPage from './PageComponents/AccountCreationPage';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import NavBar from './HeaderComponent/NavBar';
import TournamentViewPage from './PageComponents/TournamentViewPage';
import '../resources/index.css'

class App extends Component {
	render() {
		return (
			<div className='Main'>
				<NavBar />
				<Switch>
					<Route exact path="/" component={HomePage} />
					<Route exact path="/LoginPage" component={LoginPage} />
					<Route exact path="/AccountCreationPage" component={AccountCreationPage} />
					<Route exact path="/Tournaments/view" component={TournamentViewPage} />
				</Switch>
			</div>
		)
	}
}
export default App;