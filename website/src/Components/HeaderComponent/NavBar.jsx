import React, { Component } from 'react';
import { Link, Router, Redirect } from 'react-router-dom';
import { Navbar, MenuItem, Nav, NavItem, NavDropdown, Button, DropdownButton, ButtonGroup, SplitButton } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { API_URL } from '../../resources/constants.jsx';

class NavBar extends React.Component {
	constructor(){
		super();
		this.state={
			user: null,
			userLoaded: false,
			loggedOut: false,
		}
		this.handleClick = this.handleClick.bind(this);

	}

	componentWillMount(){
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
						userLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	handleClick(){
		let apiURL = API_URL + 'api/auth/logout';
		fetch(apiURL, {
			method: 'POST',
			credentials: 'include',
		})
		.then((response) => {
			if(response.ok){
				window.location.href='/LoginPage'
			}
		})
		.catch((error) => {
    		console.error(error);
    	});

	}

	render() {
		let loggedInData = null;
		if(this.state.user === null){
			loggedInData = (
				<NavItem>
					<ButtonGroup>
						<Link to="/LoginPage">
							<Button>Login</Button>
						</Link>
						<Link to="/AccountCreationPage">
							<Button bsStyle='primary'>Sign Up</Button>
						</Link>
					</ButtonGroup>
				</NavItem>
			);
		}else{
			loggedInData = (
				<NavItem>
					<Navbar.Collapse>
						<Navbar.Text onClick={ () => window.location.href='/Profile/view'}>
					    	Signed in as: {this.state.user.name}
					    </Navbar.Text>
					    <Button onClick={this.handleClick}>
					    	Logout
					    </Button>
					</Navbar.Collapse>
				</NavItem>
			);
		}
		return (
			<div className="App container">
				<Navbar>
					<Navbar.Header>
						<Navbar.Brand>
							<Link to="/">Tourneynizer</Link>
						</Navbar.Brand>
					</Navbar.Header>
					<Nav>
						<NavItem>
						    <NavDropdown 
						    className='DropDownStyle'
						      title='Tournaments'
						    >
						      <LinkContainer to="/Tournaments/view">
	    							<MenuItem eventKey="1">View Tournaments</MenuItem>
	  							</LinkContainer>
	  							<LinkContainer to="/Tournaments/create">
						      	<MenuItem eventKey="2">Create a Tournament</MenuItem>
						      </LinkContainer>
						      <LinkContainer to="/Tournaments/rules">
						      	<MenuItem eventKey="3">Tournament Rules</MenuItem>
						      </LinkContainer>
						      <MenuItem divider />
						      <MenuItem eventKey="4">About Tournaments</MenuItem>
			    			</NavDropdown>
			    		</NavItem>
			    		<NavItem>
			    			<NavDropdown
						      title='Profile'
						    >
						    	<LinkContainer to="/Profile/view">
						      	<MenuItem eventKey="1">View Your Profile</MenuItem>
						      </LinkContainer>
						      <LinkContainer to="/Profile/history">
						      	<MenuItem eventKey="2">View Your Tournament History</MenuItem>
						      </LinkContainer>
						      <LinkContainer to="/Profile/current">
						      	<MenuItem eventKey="3">View Current Matches/Teams/Requests</MenuItem>
						      </LinkContainer>
			    			</NavDropdown>
						</NavItem>
					</Nav>
					<Nav pullRight>
						{loggedInData}
					</Nav>
				</Navbar>
			</div>
			);
	}
}
export default NavBar;