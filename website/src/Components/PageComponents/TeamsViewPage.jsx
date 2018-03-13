import React, { Component } from 'react';
import { Jumbotron, Button } from 'react-bootstrap';
import ReactTable from 'react-table';
import { API_URL } from '../../resources/constants.jsx';
import { Link } from 'react-router-dom';

class TeamsViewPage extends Component{
	constructor(){
		super();
		this.state={
			teams: [],
			tournament: undefined,
			creator: undefined,
			teamsLoaded: false,
			tournamentLoaded: false,
			creatorLoaded: false,
		}
		this.requestJoin = this.requestJoin.bind(this);
	}

	getTeams(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId + '/team/all';
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						teams: json,
						teamsLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	getTournament(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId;
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						tournament: json,
						tournamentLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	//creator of tournament UPDATE
	getCreator(){
		let apiURL = API_URL + this.props.match.params.tourneyId;
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						tournament: json,
						tournamentLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	componentWillMount(){
		this.getTeams();
		this.getTournament();
	}

	//called when requesting to join a team
	requestJoin(id){
		let apiURL = API_URL + 'api/team/' + id + '/request';
		fetch(apiURL,{
			method: 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
			
		})
		.then( response => {
			if(response.status === 200){
				alert('Request Sent!');
			}else{
				alert('Error: You are already a part of this team or join request is pending.')
			}
		})
		.catch(function (error) {
			console.log(error);
		})
	}

	render(){
		const data = this.state.teams;
		const columns = [{
			Header: 'Name',
			accessor: 'name',
		},{
			Header: 'Tournament',
			Cell: row => (
				<div>
					{this.state.tournament.name}
				</div>
			)
		},{
			Header: 'Request to join',
			accessor: 'id',
			Cell: row => (
				<div>
					<center>
						<Button onClick={()=>this.requestJoin(row.value)}>Join</Button>
					</center>
				</div>
			)
		}]
		if(!(this.state.teamsLoaded && this.state.tournamentLoaded)){
			return(
				<Jumbotron><h3>There was a problem loading data</h3></Jumbotron>
			);
		}else{
			return(
				<div>
					<Jumbotron><h1>Join a Team</h1></Jumbotron>
					<ReactTable
					    data={data}
    					columns={columns}
    					className="-striped -highlight"
    				/>
				</div>
			);
		}
	}
}

export default TeamsViewPage;