import React, { Component } from 'react';
import {Jumbotron} from 'react-bootstrap';
import AccountCreationForm from './AccountCreationForm'

class AccountCreationPage extends Component {
	render() {
		return (
			<div>
				<Jumbotron>
					<h1>Create an account</h1>
					<AccountCreationForm />
				</Jumbotron>
				<form>
				</form>
			</div>
		);
	}
}
export default AccountCreationPage