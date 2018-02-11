import React, { Component } from 'react';
import { FormGroup, ControlLabel, FormControl, HelpBlock, Col } from 'react-bootstrap';

class AccountCreationForm extends Component{
	constructor(props, context) {
		super(props, context);
		this.handleChange = this.handleChange.bind(this);
		this.state = {
				email: '',
				password: '',
				confirmPassword: '',
				emailValid: false,
				passwordValid: false,
				confirmPasswordValid: false
		};
	}

	handleChange(e) {
    	this.setState({ [e.target.id]: e.target.value });
  	}

  	getEmailValidationState(){
  		if(this.state.email.indexOf('@') > -1){
  			return 'success';
  			this.setState({emailValid: true})
  		}else{
  			return 'error';
  		}
  	}

  	getPasswordValidationState(){
  		const length = this.state.password.length;
  		if(length > 6 && this.validityTesterHelper(this.state.password)){
  			return 'success';
  			this.setState({passwordValid: true})
  		}else{
  			return 'error';
  		}
  	}

  	getConfirmPasswordValidationState(){
  		const length = this.state.password.length;
  		if(length > 6 && this.validityTesterHelper(this.state.password) && this.state.password === this.state.confirmPassword){
  			return 'success';
  			this.setState({confirmPasswordValid: true})
  		}else{
  			return 'error';
  		}
  	}

  	validityTesterHelper(passwordString){
  		return /\d/.test(passwordString);
  	}

	render(){
		return(
			<form horizontal>
				<FormGroup
					controlId="firstName"
					validationState={function(){return (this.state.firstName.length > 0);}}
				>
					<Col>
					<FormControl
						type="text"
						value={this.state.firstName}
						placeholder="First Name"
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
			</form>
		);
	}
}

export default AccountCreationForm