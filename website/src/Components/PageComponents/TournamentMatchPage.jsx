import React, { Component } from 'react';
import { API_URL } from '../../resources/constants.jsx';
import ReactTable from 'react-table';
import { Jumbotron, Button } from 'react-bootstrap';

export default class TournamentMatchPage extends Component{
	constructor(){
		super();
		this.state = {
			matches: [],
			tournament: {},
			validTeams: [],
		}
		this.findTeamName = this.findTeamName.bind(this);
		this.startMatch = this.startMatch.bind(this);
		this.addPoint = this.addPoint.bind(this);
		this.getMatchScore = this.getMatchScore.bind(this);
		this.getAllMatches = this.getAllMatches.bind(this);
		this.endMatch = this.endMatch.bind(this);
		this.getRefereeTeam = this.getRefereeTeam.bind(this);
	}

	componentWillMount(){
		this.getAllMatches();
		this.getTournament();
		this.getCompleteTeams();
	}

	getAllMatches(){
		let apiURL = API_URL + 'api/tournament/' + this.props.match.params.tourneyId + '/match/getAll'
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

	getCompleteTeams(){
		let apiURL = API_URL + '/api/tournament/' + this.props.match.params.tourneyId + '/team/complete';
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
						validTeams: json,
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

	findTeamName(id){
		for(let i = 0; i < this.state.validTeams.length; i++){
			console.log(this.state.validTeams[i].id)
			console.log(id)
			if(this.state.validTeams[i].id === id){
				return this.state.validTeams[i].name;
			}
		}
	}

	getRefereeTeam(matchId){

	}

	startMatch(matchId){
		let apiURL = API_URL + 'api/tournament/' + this.state.tournament.id + '/match/' + matchId + '/start';
		fetch(apiURL, {
			method: 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
		}).then( response => {
			if(response.status === 200){
				this.getAllMatches();
			}else{
				alert('Error: Only a referee may start the match');
			}
		})
	}

	endMatch(matchId){
		this.getMatchScore(matchId).then(response => {
			if(response.ok){
				response.json().then(json =>{
					let apiURL = API_URL + 'api/tournament/' + this.state.tournament.id + '/match/' + matchId + '/end';
					let data = {
						score1: json[0],
						score2: json[1],
					}
					fetch(apiURL, {
						method: 'POST',
						headers: {
							'Accept': 'application/json',
							'Content-Type': 'application/json',
						},
						body: JSON.stringify(data),
						credentials: 'include',
					}).then( response => {
						if(response.ok){
							this.getAllMatches();	
						}else{
							alert('Error: Only a referee may end the match and matches cannot end in a tie');
						}
					})
				})
			}
		})
	}

	getMatchScore(matchID){
		let apiURL = API_URL + 'api/tournament/' + this.state.tournament.id + '/match/' + matchID + '/getScore';
		return fetch(apiURL, {
			method: 'GET',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
			credentials: 'include',
		})
	}

	addPoint(matchID, team, amount){
		this.getMatchScore(matchID).then( response => {
			if(response.ok){
				response.json().then(json => {
					let apiURL = API_URL + 'api/tournament/' + this.state.tournament.id + '/match/' + matchID + '/updateScore';
					let data = null;
					if(json[0] === null){
						json[0] = 0;
					}
					if(json[1] === null){
						json[1] = 0;
					}
					if(team === 1){
						data = {
							score1: json[0] + amount,
							score2: json[1],
						}
					}else{
						data = {
							score1: json[0],
							score2: json[1] + amount,
						}
					}
					fetch(apiURL, {
						method: 'POST',
						headers: {
							'Accept': 'application/json',
							'Content-Type': 'application/json',
						},
						body: JSON.stringify(data),
						credentials: 'include',
					}).then( response => {
						if(response.ok){
							this.getAllMatches();	
						}else{
							alert('Error: Only a referee may change the score');
						}
					})
				})
			}
		})

	}

	render(){
		if(this.state.matches.length === 0){
			return (<div><h1>There are no matches</h1></div>)
		}

		let columns = [{
			Header: 'Round',
			accessor: 'round',
		},{
			Header: original => (
				<div>Team 1: {original.data[0]._original.matchChildren.teams[0].id}</div>
				//<div>{original.row._original.teams[0]}</div>
			),
			accessor: 'score1',
			Cell: original => (
				<div>{(original.original.matchStatus === 'STARTED')
					? <div>
						{(original.original.score1 === null) ? <div>{0}</div> : <div>{original.original.score1}</div>}
						<Button onClick={() => this.addPoint(original.original.id,1,1)}>+ 1</Button>
						<Button onClick={() => this.addPoint(original.original.id,1,-1)}>- 1</Button>
					</div> 
					: <div>
						{(original.original.score1 === 0) ? <div><h1>0</h1></div> : <div>{original.original.score1}</div>}
					</div>}
				</div>
			)
		},{
			Header: original => (
				<div>Team 2: { original.data[0]._original.matchChildren.teams[1] === null ? 'TBD' : original.data[0]._original.matchChildren.teams[1].id}</div>
			),
			accessor: 'score2',
			Cell: original => (
				<div>{(original.original.matchStatus === 'STARTED')
					? <div>
						{(original.original.score2 === null) ? <div>{0}</div> : <div>{original.original.score2}</div>}
						<Button onClick={() => this.addPoint(original.original.id,2,1)}>+ 1</Button>
						<Button onClick={() => this.addPoint(original.original.id,2,-1)}>- 1</Button>
					</div> 
					: <div>
						{(original.original.score2 === 0) ? <div><h1>0</h1></div> : <div>{original.original.score2}</div>}
					</div>}
				</div>
			)
		},{
			Header: original => (
				<div>
				    <p>
				      {(() => {
				        switch (original.data[0]._original.matchStatus) {
				          case "CREATED":   return(<Button onClick={() => this.startMatch(original.data[0]._original.id)}>Start</Button>);
				          case "STARTED": return (<Button onClick={() => this.endMatch(original.data[0]._original.id)}>End</Button>);
				          default:      return (<div></div>);
				        }
				      })()}
				    </p>
				</div>
			),
		}]

		let abc = [];
		//Following for loop logic is for converting team id's into the team's name for displaying
		for (let i = 0; i < this.state.matches.length; i++) {
			let matchTemp = this.state.matches[i];
			for (let j = 0; j < matchTemp.matchChildren.teams.length; j++) {
				for(let k = 0; k < this.state.validTeams.length; k++){
					if(matchTemp.matchChildren.teams[j] != null && this.state.validTeams[k].id === matchTemp.matchChildren.teams[j].id){
						matchTemp.matchChildren.teams[j].id = this.state.validTeams[k].name;
					}else{
					}
				}
			}
			abc.push(matchTemp);
		}
		let matchTables = [];
		abc.sort((a , b) => a.id - b.id);
		for (let i = 0; i < abc.length; i++){
			let temp = [];
			temp.push(abc[i]);
			matchTables.push(<div key={i}>
						<ReactTable 
							sortable={false}
							defaultPageSize={3} 
							show data={temp} 
							showPaginationBottom={false}
							columns={columns} 
							className="-highlight"/>
					</div>);
		}

		return(
			<center>
					<div>
						<Jumbotron><h1>{this.state.tournament.name}'s Matches</h1></Jumbotron>
	    				{matchTables}
					</div>
			</center>
		);
	}
}