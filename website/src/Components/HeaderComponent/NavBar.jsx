import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Navbar, MenuItem, Nav, NavItem, NavDropdown, Button, DropdownButton, ButtonGroup, SplitButton} from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';


class NavBar extends React.Component {
	render() {
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
					      <MenuItem eventKey="4">About Tournements</MenuItem>
		    			</NavDropdown>
		    		</NavItem>
		    		<NavItem>
		    			<NavDropdown
					      title='Teams'
					    >
					    	<LinkContainer to="/Teams/view">
					      	<MenuItem eventKey="1">View My Teams</MenuItem>
					      </LinkContainer>
					      <LinkContainer to="/Teams/create">
					      	<MenuItem eventKey="2">Create a New Team</MenuItem>
					      </LinkContainer>
					      <LinkContainer to="/Teams/join">
					      	<MenuItem eventKey="3">Join a Team</MenuItem>
					      </LinkContainer>
					      <MenuItem divider />
					      <LinkContainer to="/Teams/about">
					      	<MenuItem eventKey="4">About Teams</MenuItem>
					      </LinkContainer>
		    			</NavDropdown>
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