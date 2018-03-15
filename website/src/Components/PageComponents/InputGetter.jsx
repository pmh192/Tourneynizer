import React, { Component } from 'react';
import { Button, Form, Col, ControlLabel, FormControl, FormGroup } from 'react-bootstrap';
import { API_URL } from '../../resources/constants.jsx';
/*
	A class for opening a prompt and getting user input
	props:
		getInput (function)
		displayMessage (String)
		
*/
export default class InputGetter extends Component{
	constructor(props){
		super(props);
		this.state = {
			gettingInput: false,
			inputValue: null,
			input: '',
			recipient: null,
		}
		this.handleChange = this.handleChange.bind(this);
		this.startShowing = this.startShowing.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
		this.findRecipient = this.findRecipient.bind(this);
		this.sendInvite = this.sendInvite.bind(this);
	}

	startShowing(e){
		this.setState({gettingInput: true});
		this.props.getInput(this.state.input);
		console.log('showing');
	}

	onSubmit(e){
		e.preventDefault();
		this.findRecipient();

	}

	handleChange(e){
		this.setState({ [e.target.id]: e.target.value });
	}

	findRecipient(){
		let apiURL = API_URL + 'api/user/find?email=' + this.state.input;
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					this.setState({
						recipient: json,
					}, () => this.sendInvite());
				})
			}else{
				alert('User' + this.state.input + ' Not Found');
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}


	sendInvite(){
		let apiURL = API_URL + 'api/user/' + this.state.recipient.id + '/request/team/' + this.props.teamId;
		fetch(apiURL, {
			method: 'POST',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				alert('You have sent an invite to: ' + this.state.recipient.email);
				this.setState({
					input: '',
				})
			}else{
				response.json().then( json => {
					alert(json.message)
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	render(){
		let displayComponent = (
			<div className='FormStyling'>
				<Form onSubmit={this.onSubmit}>
					<FormGroup
						controlId="input"
					>
						<Col>
						<ControlLabel>{this.props.displayMessage}</ControlLabel>
						<FormControl
							type="email"
							value={this.state.input}
							placeholder="Enter the email of the user you wish to invite..."
							onChange={this.handleChange}
						/>
						<FormControl.Feedback />
						</Col>
					</FormGroup>
					<Button type='submit'>submit</Button>
				</Form>
			</div>
		)

		return(
			<div>
				{ displayComponent }
			</div>
		);

	}
}