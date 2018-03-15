import React, { Component } from 'react';
import { API_URL } from '../../resources/constants.jsx';
import ReactTable from 'react-table';
import { Jumbotron } from 'react-bootstrap';

export default class TournamentMatchPage extends Component{
	constructor(){
		super();
		this.state = {
			matches: [],
			tournament: {},
		}
	}

	componentWillMount(){
		this.getAllMatches();
		this.getTournament();
	}

	getAllMatches(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId + '/match/getAll';
		fetch(apiURL,{
			method: 'GET',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
			
		})
		.then( response => {
			if(response.status === 200){
				response.json().then( json =>{
					this.setState({
						matches: json,
					})
				})
			}else{
				console.log('error loading matches');
			}
		})
		.catch(function (error) {
			console.log(error);
		})
	}

	getTournament(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId;
		fetch(apiURL,{
			method: 'GET',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
			
		})
		.then( response => {
			if(response.status === 200){
				response.json().then( json =>{
					console.log(json);
					this.setState({
						tournament: json,
					})
				})
			}else{
				console.log('error loading tournament');
			}
		})
		.catch(function (error) {
			console.log(error);
		})
	}

	render(){
		let columns = [{
			accessor: 'score1',
		},{
			accessor: 'score2',
		}]
		return(
			<center>
					<div>
						<Jumbotron><h1>{this.state.tournament.name}'s Matches</h1></Jumbotron>
						<ReactTable
						    data={this.state.matches}
	    					columns={columns}
	    					className="-highlight"
	    				/>
					</div>
			</center>
		);
	}
}