import React, { Component } from 'react';
import {Jumbotron} from 'react-bootstrap';
import LoginPageForm from './LoginPageForm'


class LoginPage extends Component {
	render() {
		return (
			<div className='MarginSpacer'>
				<div>
					<Jumbotron>
						<h1>Login</h1>
						<LoginPageForm />
					</Jumbotron>
				</div>
			</div>
		);
	}
}
export default LoginPage;