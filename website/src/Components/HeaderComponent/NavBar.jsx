import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {Navbar, MenuItem, Nav, NavItem, NavDropdown} from 'react-bootstrap';


class NavBar extends React.Component {
	render() {
	return (
		<Navbar>
			<Navbar.Header>
				<Navbar.Brand>
					<Link to="/">Tourneynizer</Link>
				</Navbar.Brand>
			</Navbar.Header>
			<Nav>
				<NavItem>
					<Link to="/LoginPage">Login</Link>
				</NavItem>
				<NavItem>
					<Link to="/AccountCreationPage">Sign up</Link>
				</NavItem>
				<NavDropdown eventKey={3} title="Menu" id="DropDown Menu">
					<MenuItem eventKey={3.1}>Action</MenuItem>
					<MenuItem eventKey={3.2}>Another action</MenuItem>
					<MenuItem eventKey={3.3}>Something else here</MenuItem>
					<MenuItem divider />
					<MenuItem eventKey={3.4}>Separated link</MenuItem>
				</NavDropdown>
			</Nav>
		</Navbar>
		);
	}
}
export default NavBar;