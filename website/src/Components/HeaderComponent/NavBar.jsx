import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Navbar, MenuItem, Nav, NavItem, NavDropdown, Button, DropdownButton, ButtonGroup, SplitButton } from 'react-bootstrap';
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
					      	<MenuItem eventKey="3">View Current Matches/Teams/Tournaments</MenuItem>
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