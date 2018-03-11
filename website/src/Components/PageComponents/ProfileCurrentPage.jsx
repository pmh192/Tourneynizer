import React, { Component } from 'react';
import { Jumbotron, ButtonToolbar, ToggleButton, ToggleButtonGroup } from 'react-bootstrap';

class ProfileCurrentPage extends Component{
	constructor(){
		super();
		this.state={
			displayValue: 0,
			matchesLoaded: false,
			tournamentsLoaded: false,
			teamsLoaded: false,
			matches: undefined,
			teams: undefined,
			tournaments: undefined,
		}
	}


	render(){
		return(
			<div>
				<Jumbotron>
					<h1>Current Tournaments/Matches/Teams</h1>
						<ButtonToolbar>
					  		<ToggleButtonGroup type="radio" name="options" defaultValue={1}>
					   			<ToggleButton value={1}>Radio 1 (pre-checked)</ToggleButton>
					      		<ToggleButton value={2}>Radio 2</ToggleButton>
					      		<ToggleButton value={3}>Radio 3</ToggleButton>
					    	</ToggleButtonGroup>
					  </ButtonToolbar>
				</Jumbotron>
			</div>
		);
	}
}

export default ProfileCurrentPage;