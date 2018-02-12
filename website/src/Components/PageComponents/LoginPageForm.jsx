import React, { Component } from 'react';
import { FormGroup, FormControl, Col, Button} from 'react-bootstrap';
import '../../resources/index.css'

class LoginPageForm extends Component{
	constructor(){
		super();
		this.state = {
			email: '',
			password: '',
		}
	}

	handleChange(e) {
    	this.setState({ [e.target.id]: e.target.value });
  }

	render(){
		return(
			<div className='LoginForm'>
				<form horizontal='true'>
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
							<Button type="submit">Sign up</Button>
						</Col>
				</form>
			</div>
		);
	}
}

export default LoginPageForm;