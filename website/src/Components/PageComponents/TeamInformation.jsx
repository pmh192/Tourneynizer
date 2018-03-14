import React, { Component } from 'react';
import { Jumbotron, Button, ButtonGroup } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
import { Panel } from 'react-bootstrap';
import ReactTable from 'react-table';
import { Redirect } from 'react-router';
import InputGetter from './InputGetter';


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
			requesters: [],
			prompting: false,
		}
		this.acceptTeamRequest = this.acceptTeamRequest.bind(this);
		this.removeRequest = this.removeRequest.bind(this);
		this.removeRequestAddTeam = this.removeRequestAddTeam.bind(this);
		this.rejectTeamRequest = this.rejectTeamRequest.bind(this);
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
			let reqs = [];
			if(response.ok){
				response.json().then(json => {
					reqs = json;
					const userPromises = json.map(request => {
						let apiURLUser = API_URL + 'api/user/' + request.requesterId;
						return (
							fetch(apiURLUser, {
								method: 'GET',
								credentials: 'include',
							})
						)
					})
					Promise.all(userPromises).then((responses) => {
						Promise.all(responses.map(res => res.json())).then(json => {
							let userReqs = [];

							//get requestIds and users together in object form
							for(let i = 0; i < reqs.length; i++){
								//if a match is found stop trying to find match and move on to next pair
								let found = false;
								console.log(reqs[i]);
								for(let j = 0; j < json.length && !found; j++){
									if(reqs[i].requesterId === json[j].id){
										let requestEntry = {
											requestId: reqs[i].id,
											userEmail: json[j].email,
											userName: json[j].name,
										} 
										userReqs.push(requestEntry);
										found = true;
									}
								}
							}
							console.log(userReqs);
							this.setState({
								requesters: userReqs,
							})
						})
					})
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
				this.removeRequestAddTeam(id);

			}else{
				alert('You are not the team creator');
			}
		})
		.catch((error) => {
    		console.error(error);
    	});	
	}

	removeRequestAddTeam(id) {
		let result = this.state.requesters.find(o => o.requestId === id);
		let fakeUser = {
			name: result.userName,
			email: result.userEmail,
		}
		console.log(this.state.teamMembers);
		let arr = this.state.teamMembers.slice();
		arr.push(fakeUser);
	    this.setState(prevState => ({ 
	    	teamMembers: arr, 
	    }));
	    this.removeRequest(id);
	}

	removeRequest(id) {
		this.setState(prevState => ({ 
	    	requesters: prevState.requesters.filter(request => request.requestId !== id), 
	    }));
	}

	rejectTeamRequest(id){
		let apiURL = API_URL + 'api/requests/' + id;
		fetch(apiURL, {
			method: 'DELETE',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				console.log('request' + id + ' rejected');
				this.removeRequest(id);

			}else{
				alert('You are not the team creator');
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
		if(this.state.teamLoaded && this.state.teamCreatorLoaded && this.state.teamMembersLoaded){
			let columns = [{
				id: 'userNames',
				Header: 'Name',
				accessor: 'name',
			},{
				Header: 'Email',
				accessor: 'email',
			}]
			let requestColumns = [{
				accessor: 'userName',
			},{
				accessor: 'userEmail',
			},{ //This column can be turned into a separate component
				id: 'button',
				Cell: (original) => (
					<ButtonGroup>
						<Button onClick={()=>this.acceptTeamRequest(original.row._original.requestId)}>Accept</Button>
						<Button onClick={()=>this.rejectTeamRequest(original.row._original.requestId)}>Reject</Button>
					</ButtonGroup>
				)
			}]
			return(
				<center>
					<div>
						<Jumbotron><h1>Now Viewing Team: {this.state.team.name}</h1></Jumbotron>
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
									<InputGetter getInput={(value)=> console.log(value)} displayMessage='Invite a Player'/>
								</Panel.Body>
							</Panel>
						</div>
						<div className='Members'>
							<Panel>
								<Panel.Heading><h3>Pending Requests</h3></Panel.Heading>
								<Panel.Body>
									<ReactTable 
										data={this.state.requesters} 
										columns={requestColumns}
										className='-highlight'
										defaultPageSize={10}
									/>

								</Panel.Body>
							</Panel>
						</div>
					</div>
				</center>
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