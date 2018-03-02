import React, { Component } from 'react';
import Tournament from './Tournament'
import { Link } from 'react-router-dom';

class TournamentsList extends Component{
	//will take in a js objects created from server data and render them in a table
	constructor(){
		super();
		this.state={
			tournaments: [],
			dataLoaded: false,
		}
	}

	getTournaments(){
		let apiURL = 'http://169.231.234.195:8080/api/tournament/getAll';
		console.log("executing 'getTournaments()'");
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						tournaments: json,
						dataLoaded: true,
					});
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
		if(this.state.dataLoaded === true){
			const listItems = this.state.tournaments.map(function(tourney, index){
				let tourneyType = '';
				if(tourney.type === 'VOLLEYBALL_POOLED'){
					tourneyType = 'Pooled';
				}else{
					tourneyType = 'Bracket';
				}
				const linkName = '/Tournaments/join/' + tourney.id;
				return (
					<div className='tableInfo'>
						<Tournament 
							key={tourney.id} 
							name={tourney.name} 
							startTime={tourney.startTime} 
							type={tourneyType} 
							address={tourney.address}
						/>
						
						<Link to={linkName}>
							<td>See Details</td>
						</Link>
					</div>
				);
			});
			return(
				<div>
					<div className='tourneyList'>
						{listItems}
					</div>
				</div>
			);
		}else{
			return <div><h1>Tournaments could not be displayed. Try reloading the page</h1></div>
		}
	}
}

export default TournamentsList