import React, { Component } from 'react';

class TournamentsList extends Component{
	constructor(){
		super();
		this.state={
			tournaments: [],
		}
	}

	renderTournaments(){
		const tournaments = this.state.tournaments;
		const listItems =  tournaments.map((tourney) =>
  		<li>{tourney}</li>
		);
		return (
			<ul>{listItems}</ul>
		);
	}

	render(){
		return(
			<div>
				{this.renderTournaments}
			</div>
		);
	}
}

export default TournamentsList