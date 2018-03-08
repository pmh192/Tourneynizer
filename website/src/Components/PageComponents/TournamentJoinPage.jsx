import React, { Component } from 'react';
import { Jumbotron, Button, ButtonGroup } from 'react-bootstrap';
import { GoogleMapsView } from '../CustomStyleComponents/GoogleMapsView';
import { Link } from 'react-router-dom';
import { API_URL } from '../../resources/constants.jsx'

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
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId;
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
						<ButtonGroup>
							<Link to={'/Teams/view/' + this.state.id}><Button>Join a Team</Button></Link>
							<Button>Create a team</Button>
						</ButtonGroup>
						<div><GoogleMapsView address={this.state.address}/></div>
					</Jumbotron>
				</div>
			);
		}
	}
}

export default TournamentJoinPage;