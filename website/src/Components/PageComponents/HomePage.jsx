import React, { Component } from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import NavBar from '../HeaderComponent/NavBar';
import LoginPage from './LoginPage.jsx';
import {Jumbotron} from 'react-bootstrap'
class HomePage extends Component {
	render() {
		return (
			<div>
				<Jumbotron>
					<h1>HomePage</h1>
				</Jumbotron>
			</div>
		);
	}
}
export default HomePage