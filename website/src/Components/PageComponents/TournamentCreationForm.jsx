import React, { Component } from 'react';
import { Form, FormGroup, ControlLabel, FormControl, HelpBlock, Col, Button, Panel, ButtonToolbar, ToggleButtonGroup, ToggleButton } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
import GooglePlacepicker from '../CustomStyleComponents/GooglePlacepicker';
import { DateTimePicker } from 'react-widgets';
import Moment from 'moment';
import momentLocalizer from 'react-widgets-moment';
import 'react-widgets/dist/css/react-widgets.css';


class TournamentCreationForm extends Component{

	constructor(){
		super();
		this.state = {
			tournamentName: '',
			teamSize: undefined,
			maxTeams: undefined,
			tournamentType: 0,
			latitude:0,
			longitude:0,
			date: undefined,
		}
		this.handleChange = this.handleChange.bind(this);
		this.handleRadioChange = this.handleRadioChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.getGoogleLocation = this.getGoogleLocation.bind(this);
		this.getDateTime = this.getDateTime.bind(this);
	}

	handleChange(e){
		this.setState({ [e.target.id]: e.target.value });
	}

	handleRadioChange(e){
		this.setState({
			tournamentType: e.target.value,
		})
	}

	handleSubmit(e){
    	console.log('submitting')
    	let apiURL = API_URL + 'api/tournament/create';
    	let data = {
			name: this.state.tournamentName,
			lat: this.state.latitude,
			lng: this.state.longitude,
			startTime: parseInt(this.state.date),
			teamSize: parseInt(this.state.teamSize),
			maxTeams: parseInt(this.state.maxTeams),
			type: 'VOLLEYBALL_BRACKET',
			numCourts: 0,
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
			}else{
				alert('You must be logged in to create a Tournament');
			}
		})
		.catch(function (error) {
			console.log(error);
		})
		e.preventDefault();
	}

	//callback for getting location from placepicker
	getGoogleLocation(lat, lng){
		this.setState({
			latitude:lat,
			longitude:lng,
		})
	}

	getDateTime(dt){
		//parse returned date string
		this.setState({
			date: (dt.getTime()),
		})

	}

	render(){
		Moment.locale('en');
		momentLocalizer();
		return (
			<div className='FormStyling'>
				<Form horizontal='true' onSubmit={this.handleSubmit}>
					<FormGroup
						controlId='tournamentName'
					>
						<Col>
							<ControlLabel>Tournament Name:</ControlLabel>
							<FormControl
								type="text"
								value={this.state.tournamentName}
								placeholder="Tournament Name"
								onChange={this.handleChange}
							/>
						</Col>
					</FormGroup>
					<FormGroup
						controlId='teamSize'
					>
						<Col>
							<ControlLabel>Team Size:</ControlLabel>
							<FormControl
								type="text"
								value={this.state.teamSize}
								placeholder="Team Size"
								onChange={this.handleChange}
							/>
						</Col>
					</FormGroup>
					<FormGroup
						controlId='maxTeams'
					>
						<Col>
							<ControlLabel>Max Number of Teams:</ControlLabel>
							<FormControl
								type="text"
								value={this.state.maxTeams}
								placeholder="Max Teams"
								onChange={this.handleChange}
							/>
						</Col>
					</FormGroup>
					<FormGroup
						controlId='tournamentName'
					>
						<Col>
							<ControlLabel>Tournament Type:</ControlLabel>
							<ButtonToolbar>
								<ToggleButtonGroup type="radio" name="options" defaultValue={0} >
								<ToggleButton value={0} onChange={this.handleRadioChange}>Pool</ToggleButton>
								<ToggleButton value={1} onChange={this.handleRadioChange}>Bracket</ToggleButton>
								</ToggleButtonGroup>
							</ButtonToolbar>
						</Col>
					</FormGroup>
					<FormGroup
						controlId='address'
					>
						<ControlLabel>Address:</ControlLabel>
						<Col>
							<GooglePlacepicker formStateSetter={this.getGoogleLocation}/>
						</Col>
					</FormGroup>
					<FormGroup
						controlId='date'
					>
						<ControlLabel>Date/Time:</ControlLabel>
						<Col>
							<DateTimePicker 
								onChange={this.getDateTime}
							/>
						</Col>
					</FormGroup>
					<Col>
						<Button type="submit">Create Tournament</Button>
					</Col>
				</Form>
			</div>

		);


	}
	
}

export default TournamentCreationForm;