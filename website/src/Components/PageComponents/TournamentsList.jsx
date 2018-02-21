import React, { Component } from 'react';
import Tournament from './Tournament'

class TournamentsList extends Component{
	//will take in a js objects created from server data and render them in a table
	constructor(){
		super();
		this.state={
			tournaments: [
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
				{id: 0, description: 'a sample tournament', creatorName: 'sample creator', address: '1234 Sample Street',},
			],
		}
	}

	getTournaments(){
		let apiURL = 'http://169.231.234.195:8080/api/tournament/getAll';
		console.log("hi");
		fetch(apiURL, {
			method: 'GET',
		})
		.then((response) => {
			if(response.ok){
				console.log('here');
				response.json().then(json => {
					console.log(json);
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	componentWillMount(){
		this.getTournaments();
	}

	render(){
		const listItems = this.state.tournaments.map((tourney) =>
			<Tournament id={tourney.id} description={tourney.description} creatorName={tourney.creatorName} address={tourney.address} />
		);
		return(
			<div>
				<div className='tourneyList'>{listItems}</div>
			</div>
		);
	}
}

export default TournamentsList