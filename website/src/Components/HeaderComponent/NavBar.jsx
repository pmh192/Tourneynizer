import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Navbar, MenuItem, Nav, NavItem, NavDropdown, Button, DropdownButton, ButtonGroup} from 'react-bootstrap';


class NavBar extends React.Component {
	render() {
	return (
		<Navbar>
			<Navbar.Header>
				<Navbar.Brand>
					<Link to="/">Tourneynizer</Link>
				</Navbar.Brand>
			</Navbar.Header>
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
		);
	}
}
export default NavBar;