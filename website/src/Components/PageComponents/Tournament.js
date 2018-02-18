import React, { Component } from 'react';

class Tournament extends Component{
	constructor(props){
		super(props);
		this.state={
			id: props.id,
			description: props.description,
			creatorName: props.creatorName,
			address: props.address,
		}
	}

	render(){
		return (
			<div className='tableInfo'>
				<p>Description: {this.state.description}</p>
				<p>Creator: {this.state.creatorName}</p>
				<p>Address: {this.state.address}</p>
			</div>
		);
	}
}

export default Tournament;