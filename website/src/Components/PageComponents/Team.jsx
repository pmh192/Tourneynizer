import React, { Component } from 'react';

class Team extends Component{

	constructor(){
		super();
		this.state={
			name: '',
			logo: null,
			players: [],
		}
	}

	addPlayer(player){
		const playersList= this.state.players.splice();
		playersList.append(player);
		this.setState({players: playersList});
	}

	render(){
		return(
			<div>
			</div>
		);
	}
}

//Team Players will be functional components that keep track of name stats id etc. 
//and added to list of players on team



export default Team;