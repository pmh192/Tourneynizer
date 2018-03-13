import React, { Component } from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import NavBar from '../HeaderComponent/NavBar';
import LoginPage from './LoginPage.jsx';
import {Jumbotron, Carousel} from 'react-bootstrap'
import HomePageContent from './HomePageContent.jsx'

class HomePage extends Component {

	render() {
		let welcome = <div>Welcome to Tourneynizer</div>;
		window.location.href='/Tournaments/view';
		return (
			<div>
				
				<center>
					<Jumbotron>
					<h1>Welcome to tourneynizer</h1>
					<h3>View the list of tournaments to get started!</h3>
					</Jumbotron>
				</center>
			</div>
		);
	}
}
export default HomePage