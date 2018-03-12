import React, { Component } from 'react';
import { Jumbotron, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { API_URL } from '../../resources/constants.jsx';

class ProfileAboutPage extends Component{
	constructor(){
		super();
		this.state={
			user: undefined,
			requests: undefined,
			userLoaded: false,
			requetsLoaded: false,
		}
	}

	componentWillMount(){
		this.getUser();
		this.getRequests();
	}
	getUser(){
		let apiURL = API_URL + 'api/user/get';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						user: json,
						userLoaded:true,
					});
				})
			}else{
				console.log("User not logged in")
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	//get all the users requests to respond to
	getRequests(){
		let apiURL = API_URL + 'api/user/requests/sent';
		fetch(apiURL, {
			method: 'GET',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					console.log(json);
					this.setState({
						requests: json,
						requetsLoaded: true,
					});
				})
			}else{
				console.log("User not logged in")
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	render(){
		if(!this.state.userLoaded){
			return(
				<Jumbotron><h3>Please Log in.</h3></Jumbotron>
			);
		}else{
			return(
				<div>
					<Jumbotron>
						<h1>{this.state.user.name}'s Profile</h1>
					</Jumbotron>
				</div>
			);
		}
	}
}

export default ProfileAboutPage;