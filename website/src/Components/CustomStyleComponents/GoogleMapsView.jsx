import React, { Component } from 'react';
import { Map, InfoWindow, Marker, GoogleApiWrapper } from 'google-maps-react';
//import Geocode from "react-geocode";

export class GoogleMapsView extends Component{
	constructor(props){
		super(props);

		this.state = {
			address: this.props.address,
			latitude: this.props.latitude,
			longitude: this.props.longitude,
		}
	}

	componentWillMount(){
	}

	render(){
		const style = {
			width: '50%',
			height: '50%'
		}

		return (
			<div className='googleMap' >
				<Map 
				  	google={window.google} 
				  	zoom={17}
				  	style={style}
				  	initialCenter={{
	            		lat:this.state.latitude,
	            		lng:this.state.longitude,
	          		}}

          		>
					<Marker onClick={this.onMarkerClick}
							name={'Current location'} />
				</Map>
			</div>
		);
	}

}

export default GoogleApiWrapper({
  apiKey: 'AIzaSyCKCGM8moeU_-wAUCxDDSemOtrR6Vh2FKc'
})(GoogleMapsView)