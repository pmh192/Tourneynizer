import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import TournamentsList from './TournamentsList';

class TournamentViewPage extends Component{

	render(){
		return (
			<div className='MarginSpacer'>
				<Jumbotron>
					<h1>View Tournaments</h1>
				</Jumbotron>
				<div className='TournamentList'>
					<TournamentsList />
				</div>
			</div>
		);
	}
}

export default TournamentViewPage;