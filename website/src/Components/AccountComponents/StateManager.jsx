import React, { Component } from 'react'
import { BrowserRouter, Route, Switch, IndexRedirect } from 'react-router-dom';
import TournamentViewPage from '../PageComponents/TournamentViewPage';
import TournamentCreatePage from '../PageComponents/TournamentCreatePage';
import TournamentJoinPage from '../PageComponents/TournamentJoinPage';
import TournamentRulesPage from '../PageComponents/TournamentRulesPage';
import ProfileAboutPage from '../PageComponents/ProfileAboutPage';
import ProfileCurrentPage from '../PageComponents/ProfileCurrentPage';
import ProfileHistoryPage from '../PageComponents/ProfileHistoryPage';
import HomePage from '../PageComponents/HomePage';
import LoginPage from '../PageComponents/LoginPage';
import AccountCreationPage from '../PageComponents/AccountCreationPage';
import TeamsViewPage from '../PageComponents/TeamsViewPage';
import TeamCreationPage from '../PageComponents/TeamCreationPage';
import TeamInformation from '../PageComponents/TeamInformation';


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
					<Route exact path="/" render={()=> <TournamentViewPage/>} />
					<Route exact path="/LoginPage" render={()=> <LoginPage/>}/>
					<Route exact path="/AccountCreationPage" render={()=> <AccountCreationPage />} />
					<Route exact path="/Tournaments/view" component={TournamentViewPage} />
					<Route exact path="/Tournaments/create" component={TournamentCreatePage} />
					<Route exact path="/Tournaments/join/:tourneyId" component={TournamentJoinPage} />
					<Route exact path="/Tournaments/rules" component={TournamentRulesPage} />
					<Route exact path="/Teams/view/:tourneyId" component={TeamsViewPage} />
					<Route exact path="/Teams/create/:tourneyId" component={TeamCreationPage} />
					<Route exact path="/Profile/view" component={ProfileAboutPage} />
					<Route exact path="/Profile/current" component={ProfileCurrentPage} />
					<Route exact path="/Profile/history" component={ProfileHistoryPage} />
					<Route exact path="/Profile/view/team/:teamId" component={TeamInformation} />
				</Switch>
			</div>
		);
	}
}

export default StateManager;