import React, { Component } from 'react';
import { Jumbotron, Form, FormGroup, Col, ControlLabel, FormControl, Button } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';

class TeamCreationPage extends Component{
	constructor(){
		super();
		this.state={
			name:'',
			tournament:undefined,
			tournamentLoaded: false,
		}
		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	//when component mounts, fetches the tournament data
	componentWillMount(){
		this.getTournament();
	}

	//handles change when typing in team name
	handleChange(e){
		this.setState({ [e.target.id]: e.target.value });
	}

	//handles submission of the form
	handleSubmit(e){
		console.log('submitting')
    	let apiURL = API_URL + 'api/tournament/' + this.state.tournament.id + '/team/create';
    	let data = {
			name:this.state.name,
			tournamentId:this.state.tournament.id,
    	}

    	fetch(apiURL,{
			method: 'POST',
			body: JSON.stringify(data),
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
			},
 			credentials: 'include',
			
		})
		.then( response => {
			if(response.status === 200){
				response.json().then( json => {
					console.log(json);
				})
			}
		})
		.catch(function (error) {
			console.log(error);
		})
		e.preventDefault();
	}

	//get tournament using the tournament id baked into the URL
	getTournament(){
		console.log(this.props.match.params.tourneyId);
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

	render(){
		if(!this.state.tournamentLoaded){
			return(
				<div>
					<Jumbotron><h3>There was a problem loading the tournament data</h3></Jumbotron>
				</div>
			);
		}else{
			return(
				<div>
					<Jumbotron>
						<h1>Create a Team for {this.state.tournament.name}</h1>
						<div className='FormStyling'>
							<Form horizontal='true' onSubmit={this.handleSubmit}>
								<FormGroup
									controlId='name'
								>
									<Col>
										<ControlLabel>Team Name:</ControlLabel>
										<FormControl
											type="text"
											value={this.state.name}
											placeholder="Enter a team name..."
											onChange={this.handleChange}
										/>
									</Col>
								</FormGroup>
								<Col>
									<Button type="submit">Create Team</Button>
								</Col>
							</Form>
						</div>
					</Jumbotron>
				</div>
			);
		}
	}


}





export default TeamCreationPage;