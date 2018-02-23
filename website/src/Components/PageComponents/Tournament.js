import React, { Component } from 'react';

class Tournament extends Component{
	constructor(props){
		super(props);
		this.state={
			id: this.props.id,
			name: this.props.name, 
			address: this.props.address,
			startTime: this.props.startTime,
			type: this.props.type,
		}
	}

	render(){
		return (
			<div className='tableInfo'>
				<p>Name: {this.state.name}<br/>
				StartTime:{this.state.startTime}<br/>
				Type:{this.state.type}<br/>
				Address: {this.state.address}</p>
			</div>
		);
	}
}

export default Tournament;