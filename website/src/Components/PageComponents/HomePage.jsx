import React, { Component } from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import NavBar from '../HeaderComponent/NavBar';
import LoginPage from './LoginPage.jsx';
import {jumbotron} from 'react-bootstrap'
class HomePage extends Component {
	render() {
		return (
			<div>
				<jumbotron>
					<h1>HomePage</h1>
				</jumbotron>
			</div>
		);
	}
}
export default HomePage