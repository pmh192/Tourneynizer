import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import ReactTable from 'react-table';
import { API_URL } from '../../resources/constants.jsx';

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

	render(){
		const data = this.state.teams;
		const columns = [{
			Header: 'Name',
			accessor: 'name',
		},{
			Header: 'Creator',
			Cell: row => (
				<div>
					{this.getCreator()}
				</div>
			)
		},{
			Header: 'Tournament',
			Cell: row => (
				<div>
					{this.state.tournament.name}
				</div>
			)
		},{
			Header: 'Checked in',
			accessor: 'checkedIn',
		},{
			Header: 'See Details',
			accessor: 'id',
			Cell: row => (
				<div>

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