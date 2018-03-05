import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';

class TournamentCreatePage extends Component{

	componentWillMount(){
		let apiURL='http://localhost:8080/api/tournament/create';
		 let data={
		 	"name":'test',
		 	"address":'test',
		 	"startTime":0,
		 	"teamSize":0,
		 	"maxTeams":0,
		 	"type":1,
		 	"numCourts":0,
		 }

		fetch(apiURL,{
			method: 'POST',
			body: JSON.stringify(data),
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
			
		})
	}
	render(){
		return(
			<div>
				<Jumbotron>
					<h1>Create a Tournament</h1>
				</Jumbotron>
			</div>
		);
	}
}

export default TournamentCreatePage;