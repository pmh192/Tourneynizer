import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import { GoogleMapsView } from '../CustomStyleComponents/GoogleMapsView';

class TournamentJoinPage extends Component{

	constructor(){
		super();
		this.state={
			address:'',
			name:'',
			id:0,
		}
	}

	getTournament(){
		let apiURL = 'http://169.231.234.195:8080/api/tournament/' + this.props.match.params.tourneyId;
		console.log("executing 'getTournaments()'");
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						address:json.address,
						name:json.name,
						id:json.id,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	componentWillMount(){
		this.getTournament();
	}

	render(){
		console.log(this.props.match.params.tourneyId);
		return(
			<div>
				<Jumbotron>
					<h1>Tournament Details</h1>
					<h3>You are viewing Tournament: "{this.state.name}"</h3>
					<GoogleMapsView />
				</Jumbotron>
			</div>
		);
	}
}

export default TournamentJoinPage;