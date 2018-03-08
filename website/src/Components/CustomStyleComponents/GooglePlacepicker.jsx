import React from 'react';
import PlacesAutocomplete, { geocodeByAddress, getLatLng } from 'react-places-autocomplete';
import { Form } from 'react-bootstrap';

class GooglePlacepicker extends React.Component {
	constructor(props) {
		super(props)
		this.state = { address: '' }
		this.onChange = (address) => {
			this.setState({address});
			this.props.formStateSetter(address);
		}
	}

	render() {
		const inputProps = {
			value: this.state.address,
			onChange: this.onChange,
			placeholder: 'Search for an address',
		}

		return (
			<PlacesAutocomplete inputProps={inputProps} />
		)
	}
}

export default GooglePlacepicker;