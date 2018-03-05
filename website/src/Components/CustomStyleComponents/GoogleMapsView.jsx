import React, { Component } from 'react';
import { GoogleApiWrapper } from 'google-maps-react';

class GoogleMapsView extends Component{
	constructor(){
		super();
		this.state = {

		}
	}

	render(){

	}

}

//export default GoogleMapsView;
export class MapContainer extends React.Component {}

export default GoogleMapsView({
  apiKey: AIzaSyCKCGM8moeU_-wAUCxDDSemOtrR6Vh2FKc
})(GoogleMapsView)