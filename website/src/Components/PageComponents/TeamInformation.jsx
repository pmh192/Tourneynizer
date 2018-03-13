import React, { Component } from 'react';
import { Jumbotron, Button, ButtonGroup } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
import { Panel } from 'react-bootstrap';
import ReactTable from 'react-table';
import { Redirect } from 'react-router';


export default class TeamInformation extends Component{
	constructor(){
		super();
		this.state={
			team: null,
			teamLoaded: false,
			teamCreator: null,
			teamCreatorLoaded: false,
			teamMembers: [],
			teamMembersLoaded: false,
			teamRequests: [],
			teamRequestsLoaded: false,
		}
		this.acceptTeamRequest = this.acceptTeamRequest.bind(this);
	}


	componentWillMount(){
		this.getTeam();
		this.getTeamMembers();
		this.getTeamRequests();
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

	getTeamMembers(){
		let apiURL = API_URL + 'api/team/'+ this.props.match.params.teamId + '/getMembers';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						teamMembers: json,
						teamMembersLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	getTeamRequests(){		
		let apiURL = API_URL + 'api/team/'+ this.props.match.params.teamId + '/requests/pending';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						teamRequests: json,
						teamRequestsLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});	
	}

	acceptTeamRequest(id){
		let apiURL = API_URL + 'api/requests/' + id + '/accept';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				console.log('request' + id + ' accepted');
			}else{
				alert('You are not the team creator');
			}
		})
		.catch((error) => {
    		console.error(error);
    	});	
	}

	rejectTeamRequest(id){

	}

	//-------------------------------------------------------------------------------
	//ADD COLLAPSIBLE PANELS HERE FOR SEEING TEAM LEADER, TEAM MEMBERS, TEAM REQUESTS
	//-------------------------------------------------------------------------------
	render(){
		if(this.state.teamLoaded && this.state.teamCreatorLoaded && this.state.teamMembersLoaded && this.state.teamRequestsLoaded){
			let columns = [{
				accessor: 'name',
			}]
			let requestColumns = [{
				accessor: 'requesterId',
			},{ //This column can be turned into a separate component
				id: 'button',
				Cell: (original) => (
					<ButtonGroup>
						<Button onClick={()=>this.acceptTeamRequest(original.row._original.id)}>Accept</Button>
						<Button>Reject</Button>
					</ButtonGroup>
				)
			}]
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
							<Panel.Body>
								<ReactTable 
									data={this.state.teamMembers} 
									columns={columns}
									className='-highlight'
									defaultPageSize={10}
								/>
							</Panel.Body>
						</Panel>
					</div>
					<div className='Members'>
						<Panel>
							<Panel.Heading><h3>Pending Requests</h3></Panel.Heading>
							<Panel.Body>
								<ReactTable 
									data={this.state.teamRequests} 
									columns={requestColumns}
									className='-highlight'
									defaultPageSize={10}
								/>
							</Panel.Body>
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