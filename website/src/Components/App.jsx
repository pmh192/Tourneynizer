import React, { Component } from 'react';
import NavBar from './HeaderComponent/NavBar';
import StateManager from './AccountComponents/StateManager';
import '../resources/index.css';

class App extends Component {
	render() {
		return (
			<div className='Main'>
				<NavBar />
				<StateManager />
			</div>
		);
	}
}
export default App;