import React, { Component } from 'react';
import Tournament from './Tournament'
import { Link } from 'react-router-dom';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import { API_URL } from '../../resources/constants.jsx';

class TournamentsList extends Component{
	//will take in a js objects created from server data and render them in a table
	constructor(){
		super();
		this.state={
			tournaments: [],
			dataLoaded: false,
		}
	}

	getTournaments(){
		let apiURL = API_URL + 'api/tournament/getAll';
		console.log("executing 'getTournaments()'");
		fetch(apiURL, {
			method: 'GET'
		})
		.then((response) => {
			if(response.ok){
				response.json().then(json => {
					for(let i = 0; i < json.length; i++){
						json[i].startTime = (new Date(json[i].startTime)).toString();
					}
					this.setState({
						tournaments: json,
						dataLoaded: true,
					});
				})
			}
		})
		.catch((error) => {
    		console.error(error);
    	});
	}

	componentWillMount(){
		this.getTournaments();
	}

	render(){
		if(this.state.dataLoaded === true){
			const data = this.state.tournaments;
			const columns = [
			{
				Header: 'Name',
				accessor: 'name',
			},{
				Header: 'Start Time',
				accessor: 'startTime',
			},{
				Header: 'Tourney Type',
				accessor: 'type',
			},{
				Header: 'Address',
				accessor: 'address',
			},{
				Header: 'See Details',
				accessor: 'id',
				Cell: row => (
					<div>
						<Link to={'/Tournaments/join/' + row.value}>
							<center>Details</center>
						</Link>
					</div>
				)
			}]
			return(
				<div>
					<ReactTable
					    data={data}
    					columns={columns}
    					className="-striped -highlight"
					/>
				</div>
			);
		}else{
			return( <div> </div>);
		}
	}
}

export default TournamentsList