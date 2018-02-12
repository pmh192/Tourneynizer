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
				<tr id={this.state.id}>
					<td>{this.state.description}</td>
					<td>{this.state.creatorName}</td>
					<td>{this.state.address}</td>
				</tr>
			</div>
		);
	}
}

export default Tournament;