import React, { Component } from 'react';
import { Jumbotron, Button, ButtonGroup, ButtonToolbar} from 'react-bootstrap';
import { GoogleMapsView } from '../CustomStyleComponents/GoogleMapsView';
import { Link } from 'react-router-dom';
import { API_URL } from '../../resources/constants.jsx'

class TournamentJoinPage extends Component{

	constructor(){
		super();
		this.state={
			address:'',
			latitude:0,
			longitude:0,
			name:'',
			id:0,
			dataLoaded: false,
		}
		this.startTournament = this.startTournament.bind(this);
	}

	getTournament(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId;
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						address:json.address,
						latitude:json.lat,
						longitude:json.lng,
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

	startTournament(){
		let apiURL = 'api/tournament/' + this.props.match.params.tourneyId + '/start';
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				window.location.href='/Tournaments/matches/' + this.props.match.params.tourneyId
			}else{
				alert('Could not start Tournament. Make sure you are the creator and that the tournament has not started yet.');
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
    	
	}

	render(){
		console.log(this.props.match.params.tourneyId);
		if(!this.state.dataLoaded){
			return(<div><h1>Data wasn't available</h1></div>);
		}else{
			return(
				<center>
					<div>
						<Jumbotron>
							<h1>Tournament Details</h1>
							<h3>You are viewing Tournament: "{this.state.name}"</h3>
							<div>
								<ButtonGroup>
									<Link to={'/Teams/view/' + this.state.id}><Button>Join a Team</Button></Link>
								</ButtonGroup>
								<ButtonGroup>
									<Link to={'/Teams/create/' + this.state.id}><Button>Create a team</Button></Link>
								</ButtonGroup>
								<ButtonGroup>
									<Button onClick={this.startTournament}>Start Tournament</Button>
								</ButtonGroup>
							</div>
							<div><GoogleMapsView address={this.state.address} latitude={this.state.latitude} longitude={this.state.longitude}/></div>
						</Jumbotron>
					</div>
				</center>
			);
		}
	}
}

export default TournamentJoinPage;