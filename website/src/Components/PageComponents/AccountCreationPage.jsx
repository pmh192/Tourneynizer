import React, { Component } from 'react';
import {jumbotron} from 'react-bootstrap';
import AccountCreationForm from './AccountCreationForm'

class AccountCreationPage extends Component {

	render() {
		return (
			<div>
				<jumbotron>
					<h1>Create an account</h1>
					<AccountCreationForm />
				</jumbotron>
				<form>
				</form>
			</div>
		);
	}
}
export default AccountCreationPage