import React, { Component } from 'react';
import { Jumbotron, ButtonToolbar, ToggleButton, ToggleButtonGroup } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { API_URL } from '../../resources/constants.jsx';
import ReactTable from 'react-table';

/*
* Page for viewing the user's currently active matches, tournaments, and teams
*/
class ProfileCurrentPage extends Component{
	constructor(){
		super();
		//loaded bools are set true when promise is fulfilled
		this.state={

			displayData: [],
			showing: 'teams',
			requests:[],
			tournamentsLoaded: true,
			teamsLoaded: false,
			matches: [],
			teams: [],
			tournaments: [],
		}
		this.handleRadioChange = this.handleRadioChange.bind(this);
	}

	componentWillMount(){
		this.getTournaments();
		this.getTeams();
	}

	handleRadioChange(e){
		console.log(e.target.value);
		if(parseInt(e.target.value) === 2){
			this.setState({
				displayData: this.state.teams,
				showing: 'teams',
			})
		}else if(parseInt(e.target.value) === 3){
			this.setState({
				displayData: this.state.requests,
				showing: 'requests',
			})
		}else{
			this.setState({
				displayData: [],
			})
		}
	}

	//set user's current Teams
	getTeams(){
		let apiURL = API_URL + 'api/team/getAll';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						teams: json,
						teamsLoaded:true,
						displayData: json,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	getRequests(){		
		let apiURL = API_URL + '/api/user/requests/pending';
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
								requests: userReqs,
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

	//set user's current tournaments
	getTournaments(){

	}

	render(){
		if(this.state.tournamentsLoaded && this.state.teamsLoaded){
				let columns = [];
				if(this.state.showing === 'teams'){
					columns= [{
						Header: 'Name',
						accessor: 'name',
					},{
						accessor: 'id',
						Cell: row => (
							<Link to={'/Profile/view/team/' + row.value}>Team information</Link>
						)
					}]
				}else if(this.state.showing === 'requests'){
					columns = [{
						accessor: 'userName',
					},{
						accessor: 'userEmail',
					}]
				}

			return(
				<div>
					<center>
						<Jumbotron>
							<h1>Current Tournaments/Requests/Teams</h1>
								<ButtonToolbar>
							  		<ToggleButtonGroup type="radio" name="options" defaultValue={2}>
							   			<ToggleButton onChange={this.handleRadioChange} value={1}>Tournaments</ToggleButton>
							      		<ToggleButton onChange={this.handleRadioChange} value={2}>Teams</ToggleButton>
							      		<ToggleButton onChange={this.handleRadioChange} value={3}>Requests</ToggleButton>
							    	</ToggleButtonGroup>
								</ButtonToolbar>
								<ReactTable
								    data={this.state.displayData}
			    					columns={columns}
			    					className="-striped -highlight"
			    				/>
						</Jumbotron>
					</center>
				</div>
			);
		}else{
			return(
				<div>
					<Jumbotron><h3>Please log in to view this data...</h3></Jumbotron>
				</div>
			);
		}
	}
}

export default ProfileCurrentPage;