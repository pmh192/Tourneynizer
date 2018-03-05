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
			dataLoaded: false,
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
						dataLoaded:true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	componentDidMount(){
		this.getTournament();
	}

	render(){
		console.log(this.props.match.params.tourneyId);
		if(!this.state.dataLoaded){
			return(<div><h1>Data wasn't available</h1></div>);
		}else{
			return(
				<div>
					<Jumbotron>
						<h1>Tournament Details</h1>
						<h3>You are viewing Tournament: "{this.state.name}"</h3>
						<GoogleMapsView address={this.state.address}/>
					</Jumbotron>
				</div>
			);
		}
	}
}

export default TournamentJoinPage;