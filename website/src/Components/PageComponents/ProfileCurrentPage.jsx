import React, { Component } from 'react';
import { Jumbotron, ButtonToolbar, ToggleButton, ToggleButtonGroup } from 'react-bootstrap';
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
			matchesLoaded: true,
			tournamentsLoaded: true,
			teamsLoaded: false,
			matches: [],
			teams: [],
			tournaments: [],
		}
		this.handleRadioChange = this.handleRadioChange.bind(this);
	}

	componentWillMount(){
		this.getMatches();
		this.getTournaments();
		this.getTeams();
	}

	handleRadioChange(e){
		if(parseInt(e.target.value) === 2){
			this.setState({
				displayData: this.state.teams,
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

	//set user's current tournaments
	getTournaments(){

	}

	//set user's current matches
	getMatches(){

	}

	render(){
		if(this.state.tournamentsLoaded && this.state.teamsLoaded && this.state.matchesLoaded){
			let columns= [{
				Header: 'Name',
				accessor: 'name',
			}]

			return(
				<div>
					<Jumbotron>
						<h1>Current Tournaments/Matches/Teams</h1>
							<ButtonToolbar>
						  		<ToggleButtonGroup type="radio" name="options" defaultValue={2}>
						   			<ToggleButton onChange={this.handleRadioChange} value={1}>Tournaments</ToggleButton>
						      		<ToggleButton onChange={this.handleRadioChange} value={2}>Teams</ToggleButton>
						      		<ToggleButton onChange={this.handleRadioChange} value={3}>Matches</ToggleButton>
						    	</ToggleButtonGroup>
							</ButtonToolbar>
							<ReactTable
							    data={this.state.displayData}
		    					columns={columns}
		    					className="-striped -highlight"
		    				/>
					</Jumbotron>
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