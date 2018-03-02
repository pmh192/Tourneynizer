import React, { Component } from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import TournamentViewPage from '../PageComponents/TournamentViewPage';
import TournamentCreatePage from '../PageComponents/TournamentCreatePage';
import TournamentJoinPage from '../PageComponents/TournamentJoinPage';
import TournamentRulesPage from '../PageComponents/TournamentRulesPage';
import TeamsViewPage from '../PageComponents/TeamsViewPage';
import TeamsCreatePage from '../PageComponents/TeamsCreatePage';
import TeamsJoinPage from '../PageComponents/TeamsJoinPage';
import TeamsAboutPage from '../PageComponents/TeamsAboutPage';
import HomePage from '../PageComponents/HomePage';
import LoginPage from '../PageComponents/LoginPage';
import AccountCreationPage from '../PageComponents/AccountCreationPage';


class StateManager extends Component{
	constructor(){
		super();
		this.state = {
			loggedIn: false,
			email: '',
			name: '',
		};

		this.getUserInfo = this.getUserInfo.bind(this);
		this.update = this.update.bind(this);
	}

	getUserInfo(data){
		this.setState({
			email: data.email,
			name: data.name,
			loggedIn: true,
		})
	}

	update(){
		console.log('updating');
		return {
			email: this.state.email,
			name: this.state.name,
			loggedIn: this.state.loggedIn,
		};
	}

	render(){
		return (
			<div id='routing'>

				<Switch>
					<Route exact path="/" render={()=> <HomePage update={this.update}/>} />
					<Route exact path="/LoginPage" render={()=> <LoginPage getUserInfo={this.getUserInfo} />}/>
					<Route exact path="/AccountCreationPage" render={()=> <AccountCreationPage />} />
					<Route exact path="/Tournaments/view" component={TournamentViewPage} />
					<Route exact path="/Tournaments/create" component={TournamentCreatePage} />
					<Route exact path="/Tournaments/join/:tourneyId" component={TournamentJoinPage} />
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

export default StateManager;