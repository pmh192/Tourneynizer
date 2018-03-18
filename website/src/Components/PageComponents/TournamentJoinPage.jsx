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
			status: null,
		}
		this.startTournament = this.startTournament.bind(this);
	}

	getTournament(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId;
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						//in the future just make a tournament variable and set that to json
						address:json.address,
						latitude:json.lat,
						longitude:json.lng,
						name:json.name,
						id:json.id,
						dataLoaded:true,
						status: json.status,
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
		console.log(this.props.match.params.tourneyId);
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId + '/start';
		fetch(apiURL, {
			method: 'POST',
			credentials: 'include',
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
			let started = null;
			if(this.state.status === 'CREATED'){
				started = (<div>
					<ButtonGroup>
						<Link to={'/Teams/view/' + this.state.id}><Button>Join a Team</Button></Link>
					</ButtonGroup>
					<ButtonGroup>
						<Link to={'/Teams/create/' + this.state.id}><Button>Create a team</Button></Link>
					</ButtonGroup>
					<ButtonGroup>
						<Link to={'/Tournaments/matches/' + this.state.id}><Button>View Matches</Button></Link>
					</ButtonGroup>
					<ButtonGroup>
						<Button onClick={this.startTournament}>Start Tournament</Button>
					</ButtonGroup>
				</div>)
			}else if(this.state.status === 'STARTED'){
				started = (<div>
					<ButtonGroup>
						<h3>Tournament In Progress</h3>
						<Link to={'/Tournaments/matches/' + this.state.id}><Button>View Matches</Button></Link>
					</ButtonGroup>
				</div>)
			}else{
				started = (<div>
					<ButtonGroup>
						<h3>Tournament Finished</h3>
						<Link to={'/Tournaments/matches/' + this.state.id}><Button>View Matches</Button></Link>
					</ButtonGroup>
				</div>)
			}
			return(
				<center>
					<div>
						<Jumbotron>
							<h1>Tournament Details</h1>
							<h3>You are viewing Tournament: "{this.state.name}"</h3>
							{started}
							<div><GoogleMapsView address={this.state.address} latitude={this.state.latitude} longitude={this.state.longitude}/></div>
						</Jumbotron>
					</div>
				</center>
			);
		}
	}
}

export default TournamentJoinPage;