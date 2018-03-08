//Utility functions used throughout the application
import { API_URL } from './constants.jsx';

export function getTournament(id) {
	let apiURL = 'http://169.231.234.195:8080/api/tournament/' + id;
	let data = undefined;
	return fetch(apiURL, {
		method: 'GET'
	})
	.then((response) => {
		if(response.ok){
			response.json().then(json => {
				console.log(json);
				data = json;
				return data;
			})
		}
		return data;
	})
	.catch((error) => {
		console.error(error);
	});

	return data;
}