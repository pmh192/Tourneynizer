import React, { Component } from 'react';
import {Jumbotron} from 'react-bootstrap';
import AccountCreationForm from './AccountCreationForm'

class AccountCreationPage extends Component {
	render() {
		return (
			<center>
				<div className= 'MarginSpacer'>
					<Jumbotron>
						<h1>Create an account</h1>
						<AccountCreationForm />
					</Jumbotron>
					<form>
					</form>
				</div>
			</center>
		);
	}
}
export default AccountCreationPage