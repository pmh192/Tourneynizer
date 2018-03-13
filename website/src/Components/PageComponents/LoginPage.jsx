import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';
import LoginPageForm from './LoginPageForm'


class LoginPage extends Component {
	constructor(props){
		super(props);
		this.state = {
		}

	}

	render() {
		return (
			<div className='MarginSpacer'>
				<div>
					<center>
						<Jumbotron>
							<h1>Login</h1>
							<LoginPageForm getUserInfo={this.props.getUserInfo}/>
						</Jumbotron>
					</center>
				</div>
			</div>
		);
	}
}
export default LoginPage;