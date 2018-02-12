import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';

class TournamentViewPage extends Component{

	render(){
		return (
			<div className='MarginSpacer'>
				<Jumbotron>
					<h1>View Tournaments</h1>
				</Jumbotron>
			</div>
		);
	}
}

export default TournamentViewPage;