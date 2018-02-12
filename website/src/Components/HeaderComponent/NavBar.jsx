import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Navbar, MenuItem, Nav, NavItem, NavDropdown, Button, DropdownButton, ButtonGroup, SplitButton} from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import '../../resources/index.css'

class NavBar extends React.Component {
	render() {
	return (
		<div>
			<Navbar>
				<Navbar.Header>
					<Navbar.Brand>
						<Link to="/">Tourneynizer</Link>
					</Navbar.Brand>
				</Navbar.Header>
				<Nav>
					<NavItem>
						<ButtonGroup>
					    <SplitButton
					      title='Tournaments'
					    >
					      <LinkContainer to="/Tournaments/view">
    							<MenuItem eventKey={1}>View Tournaments</MenuItem>
  							</LinkContainer>
					      <MenuItem eventKey="2">Create a Tournament</MenuItem>
					      <MenuItem eventKey="3">Tournament Rules</MenuItem>
					      <MenuItem divider />
					      <MenuItem eventKey="4">About Tournements</MenuItem>
		    			</SplitButton>
		    			<SplitButton
					      title='Teams'
					    >
					      <MenuItem eventKey="1">View My Team</MenuItem>
					      <MenuItem eventKey="2">Create a New Team</MenuItem>
					      <MenuItem eventKey="3">Join a Team</MenuItem>
					      <MenuItem divider />
					      <MenuItem eventKey="4">About Teams</MenuItem>
		    			</SplitButton>
	    			</ButtonGroup>
					</NavItem>
				</Nav>
				<Nav pullRight>
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
				</Nav>
			</Navbar>
		</div>
		);
	}
}
export default NavBar;