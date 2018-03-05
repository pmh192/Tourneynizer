import React, { Component } from 'react';
import { Map, InfoWindow, Marker, GoogleApiWrapper } from 'google-maps-react';
import Geocode from "react-geocode";

export class GoogleMapsView extends Component{
	constructor(props){
		super(props);

		this.state = {
			address: this.props.address,
			latitude: 0,
			longitude: 0,
		}
	}

	componentWillMount(){

	}

	getLatLong(){
		Geocode.fromAddress(this.state.address).then(
			response => {
				const { lat, lng } = response.results[0].geometry.location;
				console.log(lat, lng);
				this.setState({
					latitude: lat,
					longitude: lng,
				});
		  	},
			error => {
				console.error(error);
			}
		);
	}

	render(){
		this.getLatLong();
		return (
			<div className='googleMap' >
				<Map 
				  	google={window.google} 
				  	zoom={5}
				  	initialCenter={{
	            		lat:this.state.latitude,
	            		lng:this.state.longitude,
	          		}}

          		>
				<Marker onClick={this.onMarkerClick}
						name={'Current location'} />
			  </Map>
			</div>
		)
	}

}

export default GoogleApiWrapper({
  apiKey: 'AIzaSyCKCGM8moeU_-wAUCxDDSemOtrR6Vh2FKc'
})(GoogleMapsView)