import React, { Component } from 'react';
import { Map, InfoWindow, Marker, GoogleApiWrapper } from 'google-maps-react';

export class GoogleMapsView extends Component{
	constructor(){
		super();
		this.state = {
			selectedPlace: {
				name: 'Santa Barbara, CA 93106'
			}
		}
	}

	render(){
		return (
		  <Map google={window.google} zoom={14}>
			<Marker onClick={this.onMarkerClick}
					name={'Current location'} />
			<InfoWindow onClose={this.onInfoWindowClose}>
			</InfoWindow>
		  </Map>
		)
	}

}

export default GoogleApiWrapper({
  apiKey: 'AIzaSyCKCGM8moeU_-wAUCxDDSemOtrR6Vh2FKc'
})(GoogleMapsView)