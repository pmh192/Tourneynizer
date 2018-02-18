import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';

function PageHeader(props){
	return (
		<div className='pageHead'>
			<Jumbotron><h1>{props.title}</h1></Jumbotron>
		</div>
	);
}

export default PageHeader;