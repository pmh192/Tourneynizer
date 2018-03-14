import React, { Component } from 'react';
import { Button, Form, Col, ControlLabel, FormControl, FormGroup } from 'react-bootstrap';

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
		}
		this.handleChange = this.handleChange.bind(this);
		this.startShowing = this.startShowing.bind(this);
	}

	startShowing(e){
		this.setState({gettingInput: true});
		this.props.getInput(this.state.input);
		console.log('showing');
	}

	handleChange(e){
		this.setState({ [e.target.id]: e.target.value });
	}

	render(){
		let displayComponent = null;
		console.log(this.state.gettingInput);
		displayComponent = (
			<div className='FormStyling'>
				<Form onSubmit={this.props.getInput(this.state.input)}>
					<FormGroup
						controlId="firstName"
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