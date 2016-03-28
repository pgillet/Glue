'use strict';

// tag::vars[]
const React = require('react');
const client = require('./client');
// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {websites: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/api/eventWebsites'}).done(response => {
			this.setState({websites: response.entity._embedded.eventWebsites});
		});
	}

	render() {
		return (
			<WebsiteList websites={this.state.websites}/>
		)
	}
}
// end::app[]

// tag::website-list[]
class WebsiteList extends React.Component{
	render() {
		var websites = this.props.websites.map(eventWebsite =>
			<EventWebsite key={eventWebsite._links.self.href} eventWebsite={eventWebsite}/>
		);
		return (
			<table>
				<tr>
					<th>Venue Name</th>
					<th>URL</th>
					<th></th>
				</tr>
				{websites}
			</table>
		)
	}
}
// end::website-list[]

// tag::website[]
class EventWebsite extends React.Component{
	render() {
		return (
			<tr>
				<td></td>
				<td>{this.props.eventWebsite.uri}</td>
				<td></td>
			</tr>
		)
	}
}
// end::website[]

// tag::render[]
React.render(
	<App />,
	document.getElementById('react')
)
// end::render[]

