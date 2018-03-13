import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import TournamentsList from './TournamentsList';

class TournamentViewPage extends Component{
	constructor(){
		super();
		this.state={

		}
	}

	render(){
		return (
			<div className='MarginSpacer'>
				<center>
					<Jumbotron>
						<h1>Tournaments</h1>
						<div className='TournamentList'>
							<TournamentsList />
						</div>
					</Jumbotron>
				</center>
			</div>
		);
	}
}

export default TournamentViewPage;