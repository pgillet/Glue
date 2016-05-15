'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

var Form = require('react-jsonschema-form').default;

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {eventWebsites: [], attributes: {}, pageSize: 10, links: {}};
		this.updatePageSize = this.updatePageSize.bind(this);
		this.search = this.search.bind(this);
		this.onCreate = this.onCreate.bind(this);
		this.onDelete = this.onDelete.bind(this);
		this.onNavigate = this.onNavigate.bind(this);
	}

	// tag::follow-2[]
	loadFromServer(pageSize) {
		follow(client, root, [
			{rel: 'eventWebsites', params: {size: pageSize}}]
		).then(websiteCollection => {
			return client({
				method: 'GET',
				path: websiteCollection.entity._links.profile.href,
				headers: {'Accept': 'application/schema+json'}
			}).then(schema => {
				this.schema = schema.entity;
				return websiteCollection;
			});
		}).done(websiteCollection => {
			this.setState({
				eventWebsites: websiteCollection.entity._embedded.eventWebsites,
				attributes: this.schema,
				pageSize: pageSize,
				links: websiteCollection.entity._links});
		});
		
	}
	// end::follow-2[]

	// tag::create[]
	onCreate(newEventWebsite) {
		follow(client, root, ['eventWebsites']).then(websiteCollection => {
			return client({
				method: 'POST',
				path: websiteCollection.entity._links.self.href,
				entity: newEventWebsite,
				headers: {'Content-Type': 'application/json'}
			})
		}).then(response => {
			return follow(client, root, [
				{rel: 'eventWebsites', params: {'size': this.state.pageSize}}]);
		}).done(response => {
			if(this.state.eventWebsites.length < this.state.pageSize)
				this.loadFromServer(this.state.pageSize);
			else
				this.onNavigate(response.entity._links.last.href);
		});
	}
	// end::create[]

	// tag::delete[]
	onDelete(eventWebsite) {
		client({method: 'DELETE', path: eventWebsite._links.self.href}).done(response => {
			this.loadFromServer(this.state.pageSize);
		});
	}
	// end::delete[]

	// tag::navigate[]
	onNavigate(navUri) {
		client({method: 'GET', path: navUri}).done(websiteCollection => {
			this.setState({
				eventWebsites: websiteCollection.entity._embedded.eventWebsites,
				attributes: this.state.attributes,
				pageSize: this.state.pageSize,
				links: websiteCollection.entity._links
			});
		});
	}
	// end::navigate[]

	// tag::update-page-size[]
	updatePageSize(pageSize) {
		if (pageSize !== this.state.pageSize) {
			this.loadFromServer(pageSize);
		}
	}
	// end::update-page-size[]
	
	search(terms) {
		follow(client, root, [
  			'eventWebsites', 'search', {rel: 'findByUri', params: {'uri': terms}}]
  		).done(websiteCollection => {
  			this.setState({
  				eventWebsites: websiteCollection.entity._embedded.eventWebsites,
  				attributes: this.schema,
  				pageSize: this.state.pageSize,
  				links: websiteCollection.entity._links});
  		});
	}

	// tag::follow-1[]
	componentDidMount() {
		this.loadFromServer(this.state.pageSize);
	}
	// end::follow-1[]

	render() {
		return (
			<div className="container">
				<div className="row">
					<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
					<WebsiteList eventWebsites={this.state.eventWebsites}
								  links={this.state.links}
								  pageSize={this.state.pageSize}
								  onNavigate={this.onNavigate}
								  onDelete={this.onDelete}
								  updatePageSize={this.updatePageSize}
								  search={this.search}/>
				</div>
			</div>
		)
	}
}

// tag::create-dialog[]
class CreateDialog extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(e) {
		this.props.onCreate(e.formData);

		// Navigate away from the dialog to hide it.
		window.location = "#";
	}

	render() {		
		
		return (
			<div>
				<a href="#createEventWebsite">Create</a>

				<div id="createEventWebsite" className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Create new eventWebsite</h2>
					
						<Form schema={this.props.attributes} onSubmit={this.handleSubmit}>
							<button>Submit</button>
						</Form>
					</div>
				</div>
			</div>
		)
	}

}
// end::create-dialog[]

class WebsiteList extends React.Component {

	constructor(props) {
		super(props);
		this.handleNavFirst = this.handleNavFirst.bind(this);
		this.handleNavPrev = this.handleNavPrev.bind(this);
		this.handleNavNext = this.handleNavNext.bind(this);
		this.handleNavLast = this.handleNavLast.bind(this);
		this.handleInput = this.handleInput.bind(this);
		this.handleSearch = this.handleSearch.bind(this);
	}

	// tag::handle-page-size-updates[]
	handleInput(e) {
		e.preventDefault();
		var pageSize = this.refs.pageSize.value;
		if (/^[0-9]+$/.test(pageSize)) {
			this.props.updatePageSize(pageSize);
		} else {
			this.refs.pageSize.value =
				pageSize.substring(0, pageSize.length - 1);
		}
	}
	// end::handle-page-size-updates[]
	
	handleSearch(e) {
		e.preventDefault();
		var terms = this.refs.search.value;
		this.props.search(terms)
	}

	// tag::handle-nav[]
	handleNavFirst(e){
		e.preventDefault();
		this.props.onNavigate(this.props.links.first.href);
	}

	handleNavPrev(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.prev.href);
	}

	handleNavNext(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.next.href);
	}

	handleNavLast(e) {
		e.preventDefault();
		this.props.onNavigate(this.props.links.last.href);
	}
	// end::handle-nav[]

	// tag::eventWebsite-list-render[]
	render() {
		var eventWebsites = this.props.eventWebsites.map(eventWebsite =>
			<EventWebsite key={eventWebsite._links.self.href} eventWebsite={eventWebsite} onDelete={this.props.onDelete}/>
		);

		var navLinks = [];
		if ("first" in this.props.links) {
			navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
		}
		if ("prev" in this.props.links) {
			navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
		}
		if ("next" in this.props.links) {
			navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
		}
		if ("last" in this.props.links) {
			navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
		}

		return (
			<div>
				<form onSubmit={this.handleSearch}>
					<input type="search" ref="search" />
					<input type="submit" value="Search" />
				</form>
				<label for="pageSize">Page size: </label>
				<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput} size="2"/>
				<table>
					<tr>
						<th>Venue name</th>
						<th>URL</th>
						<th></th>
						<th></th>
					</tr>
					{eventWebsites}
				</table>
				<div>
					{navLinks}
				</div>
			</div>
		)
	}
	// end::eventWebsite-list-render[]
}

// tag::eventWebsite[]
class EventWebsite extends React.Component {

	constructor(props) {
		super(props);
		this.handleDelete = this.handleDelete.bind(this);
	}

	handleDelete() {
		this.props.onDelete(this.props.eventWebsite);
	}

	render() {
		return (
			<tr>
				<td></td>
				<td>{this.props.eventWebsite.uri}</td>
				<td></td>
				<td>
					<button onClick={this.handleDelete}>Delete</button>
				</td>
			</tr>
		)
	}
}
// end::eventWebsite[]

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
