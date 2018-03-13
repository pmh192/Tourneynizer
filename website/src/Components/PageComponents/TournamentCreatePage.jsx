import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
import TournamentCreationForm from './TournamentCreationForm';

class TournamentCreatePage extends Component{
	render(){
		return(
			<center>
				<div className='MarginSpacer'>
					<Jumbotron>
						<h1>Create a Tournament</h1>
						<TournamentCreationForm />
					</Jumbotron>
				</div>
			</center>
		);
	}
}

export default TournamentCreatePage;