import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
import { Panel } from 'react-bootstrap';

export default class TeamInformation extends Component{
	constructor(){
		super();
		this.state={
			team: null,
			teamLoaded: false,
			teamCreator: null,
			teamCreatorLoaded: false,
		}
	}


	componentWillMount(){
		this.getTeam();
	}

	getTeam(){
		let apiURL = API_URL + 'api/team/' + this.props.match.params.teamId;
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						team: json,
						teamLoaded:true,
					});
					this.getTeamLeader(json.creatorId);
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	getTeamLeader(id){
		let apiURL = API_URL + 'api/user/' + id;
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						teamCreator: json,
						teamCreatorLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	//-------------------------------------------------------------------------------
	//ADD COLLAPSIBLE PANELS HERE FOR SEEING TEAM LEADER, TEAM MEMBERS, TEAM REQUESTS
	//-------------------------------------------------------------------------------
	render(){
		if(this.state.teamLoaded && this.state.teamCreatorLoaded){
			return(
				<div>
					<Jumbotron><h1>Now Viewing Team:</h1></Jumbotron>
					<div className='TeamCreator:'>
						<Panel>
							<Panel.Heading>
								<h3>Team Creator: {this.state.teamCreator.name}</h3>
							</Panel.Heading>
						</Panel>
					</div>
					<div className='Members'>
						<Panel>
							<Panel.Heading><h3>Team Members</h3></Panel.Heading>
						</Panel>
					</div>
				</div>
			);
		}else{
			return(
				<div>
					<Jumbotron><h3>No team information was found. Please verify you are logged in.</h3></Jumbotron>
				</div>
			)
		}
	}
}