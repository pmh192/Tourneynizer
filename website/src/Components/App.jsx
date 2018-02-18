import React, { Component } from 'react';
import HomePage from './PageComponents/HomePage';
import LoginPage from './PageComponents/LoginPage';
import AccountCreationPage from './PageComponents/AccountCreationPage';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import NavBar from './HeaderComponent/NavBar';
import TournamentViewPage from './PageComponents/TournamentViewPage';
import TournamentCreatePage from './PageComponents/TournamentCreatePage';
import TournamentRulesPage from './PageComponents/TournamentRulesPage';
import TeamsViewPage from './PageComponents/TeamsViewPage';
import TeamsCreatePage from './PageComponents/TeamsCreatePage';
import TeamsJoinPage from './PageComponents/TeamsJoinPage';
import TeamsAboutPage from './PageComponents/TeamsAboutPage';
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
					<Route exact path="/Tournaments/create" component={TournamentCreatePage} />
					<Route exact path="/Tournaments/rules" component={TournamentRulesPage} />
					<Route exact path="/Teams/view" component={TeamsViewPage} />
					<Route exact path="/Teams/create" component={TeamsCreatePage} />
					<Route exact path="/Teams/view" component={TeamsJoinPage} />
					<Route exact path="/Teams/about" component={TeamsAboutPage} />
				</Switch>
			</div>
		);
	}
}
export default App;