import React, { Component } from 'react';
import { Jumbotron, Image, Grid, Row, Col } from 'react-bootstrap';
import cone from '../../resources/traffic-cone-icon.png';

class ConstructionMessage extends Component{

	render(){
		return(
			<div className='ConstructMessage'>
				<h3>Sorry, this page is under development.</h3>
				<Image src={cone} rounded/>

			</div>
		);
	}

}

export default ConstructionMessage;