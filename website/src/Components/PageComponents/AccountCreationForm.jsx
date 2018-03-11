import React, { Component } from 'react';
import { Form, FormGroup, ControlLabel, FormControl, HelpBlock, Col, Button, Panel } from 'react-bootstrap';
import '../../resources/index.css';
import { API_URL } from '../../resources/constants.jsx';

class AccountCreationForm extends Component{
	constructor(props, context) {
		super(props, context);
		this.handleChange = this.handleChange.bind(this);
		this.state = {
			email: '',
			password: '',
			confirmPassword: '',
			firstName: '',
			lastName: '',
			submitted: false,
		};

		this.onSubmit = this.onSubmit.bind(this);
	}

	handleChange(e) {
		this.setState({ [e.target.id]: e.target.value });
	}

	//later implement with email verification
	getEmailValidationState(){
		if(this.state.email.indexOf('@') > -1){
			return 'success';
		}else{
			return 'error';
		}
	}

	getPasswordValidationState(){
		const length = this.state.password.length;
		if(length >= 6 && this.validityTesterHelper(this.state.password)){
			return 'success';
		}else{
			return 'error';
		}
	}

	getConfirmPasswordValidationState(){
		const length = this.state.password.length;
		if(length >= 6 && this.validityTesterHelper(this.state.password) && this.state.password === this.state.confirmPassword){
			return 'success';
		}else{
			return 'error';
		}
	}

	getNameValidationState(){
		if(this.state.firstName.length > 0){
			return 'success';
		}else{
			return 'error';
		}
	}

	validityTesterHelper(passwordString){
		return /\d/.test(passwordString);
	}

	onSubmit(e){

		//send user creation request to server
		if(
			this.getConfirmPasswordValidationState() === 'success' && 
			this.getPasswordValidationState() === 'success' && 
			this.getEmailValidationState() === 'success'
		){
			let fullName = this.state.firstName + ' ' + this.state.lastName;
			var data = {
				email: this.state.email,
				name: fullName,
				password: this.state.password,
			};
			fetch(API_URL + 'api/user/create', {
					method: 'POST',
					body: JSON.stringify(data),
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json',
					},
					include:'credentials',
			})
			.then((response) => {
				if(response.status === 200){
					//if server successfully creates the account
					alert('Account created!');
					this.setState({
						submitted: true,
					})
				}else{
					alert('Error with account creation');
				}
			})
			.catch(function (error) {
				console.log(error);
			})
		}
		e.preventDefault();
	}

	render(){
		if(!this.state.submitted){
			return(
				<div  className='FormStyling'>
					<Form horizontal='true' onSubmit={(e) => this.onSubmit(e)}>
						<FormGroup
							controlId="firstName"
							validationState={this.getNameValidationState()}
						>
							<Col>
							<FormControl
								type="text"
								value={this.state.firstName}
								placeholder="First Name"
								onChange={this.handleChange}
							/>
							<FormControl.Feedback />
							</Col>
						</FormGroup>
						<FormGroup
							controlId="lastName"
						>
							<Col>
							<FormControl
								type="text"
								value={this.state.lastName}
								placeholder="Last Name"
								onChange={this.handleChange}
							/>
							</Col>
						</FormGroup>
						<FormGroup
							controlId="email"
							validationState={this.getEmailValidationState()}
						>
							<Col>
							<FormControl
								type="email"
								value={this.state.email}
								placeholder="Email address"
								onChange={this.handleChange}
							/>
							<FormControl.Feedback />
							<HelpBlock>Email address must be valid</HelpBlock>
							</Col>
						</FormGroup>
						<FormGroup
							controlId="password"
							validationState={this.getPasswordValidationState()}
						>
							<Col>
							<FormControl
								type="password"
								value={this.state.password}
								placeholder="Enter Your Password"
								onChange={this.handleChange}
							/>
							<FormControl.Feedback />
							<HelpBlock>Password must be at least 6 characters and contain at least one number</HelpBlock>
							</Col>
						</FormGroup>
						<FormGroup
							controlId="confirmPassword"
							validationState={this.getConfirmPasswordValidationState()}
						>
							<Col>
							<FormControl
								type="Password"
								value={this.state.confirmPassword}
								placeholder="Re-enter Your Password"
								onChange={this.handleChange}
							/>
							<FormControl.Feedback />
							<HelpBlock>Password must match previously entered password</HelpBlock>
							</Col>
						</FormGroup>
						<Button type="submit">Sign up</Button>
					</Form>
				</div>
			);
		}else{
			window.location.reload();
		}
	}
}

export default AccountCreationForm