import React, { Component } from 'react';
import { Form, FormGroup, FormControl, Col, Button} from 'react-bootstrap';
import '../../resources/index.css';
import { API_URL } from '../../resources/constants.jsx';

class LoginPageForm extends Component{
	constructor(){
		super();
		this.state = {
			email: '',
			password: '',
			name: '',
		}
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleChange = this.handleChange.bind(this);
	}

	handleChange(e) {
    	this.setState({ [e.target.id]: e.target.value });
    }

    handleSubmit(e) {
    	console.log('submitting')
    	let apiURL = API_URL + 'api/auth/login';
    	let data = {
    		email: this.state.email,
    		password: this.state.password,
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
					this.props.getUserInfo(json);
				})
			}
		})
		.catch(function (error) {
			console.log(error);
		})
		console.log(this.state.email);
		console.log(document.cookie);
		e.preventDefault();
    }

	render(){
		return(
			<div className='FormStyling'>
				<Form horizontal='true' onSubmit={this.handleSubmit}>
					<FormGroup
						controlId="email"
						>
						<Col>
							<FormControl
								type="email"
								value={this.state.email}
								placeholder="Email address"
								onChange={this.handleChange}
							/>
						</Col>
					</FormGroup>
					<FormGroup
						controlId="password"
						>
						<Col>
							<FormControl
								type="password"
								value={this.state.password}
								placeholder="Enter Your Password"
								onChange={this.handleChange}
							/>
						</Col>
					</FormGroup>
					<Col>
						<Button type="submit">Sign in</Button>
					</Col>
				</Form>
			</div>
		);
	}
}

export default LoginPageForm;