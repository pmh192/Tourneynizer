import React, { Component } from 'react';
import { Jumbotron } from 'react-bootstrap';

class TournamentRulesPage extends Component{
	render(){
		return(
			<div>
				<Jumbotron>
					<h1>Tournament Rules Document</h1>
					<div className='tournamentRules'>
						<embed 
							src="https://cbva.com/UploadDocuments/CBVADocs/CBVARulebook.pdf" 
							width="1000" 
							height="600" 
							type='application/pdf'>
						</embed>
					</div>
				</Jumbotron>
			</div>
		);
	}
}

export default TournamentRulesPage;