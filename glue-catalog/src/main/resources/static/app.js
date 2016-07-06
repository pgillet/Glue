'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const when = require('when');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/api';

var Form = require('react-jsonschema-form').default;

var SockJS = require('sockjs-client');
require('stompjs');

var stompClient = null;

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {eventWebsites: [], attributes: {}, pageSize: 10, links: {}};
		this.updatePageSize = this.updatePageSize.bind(this);
		this.search = this.search.bind(this);
		this.onCreate = this.onCreate.bind(this);
		this.onUpdate = this.onUpdate.bind(this);
		this.onDelete = this.onDelete.bind(this);
		this.onNavigate = this.onNavigate.bind(this);
		this.connect = this.connect.bind(this);
		this.disconnect = this.disconnect.bind(this);
		this.showGreeting = this.showGreeting.bind(this);
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
				
				// TODO Manager: read-only property
				delete schema.entity.properties.manager;
				
				this.schema = schema.entity;
				this.links = websiteCollection.entity._links;
				return websiteCollection;
			});
		}).then(websiteCollection => {
			return websiteCollection.entity._embedded.eventWebsites.map(website =>
					client({
						method: 'GET',
						path: website._links.self.href
					})
			);
		}).then(websitePromises => {
			return when.all(websitePromises);
		}).done(eventWebsites => {
			this.setState({
				eventWebsites: eventWebsites,
				attributes: this.schema,
				pageSize: pageSize,
				links: this.links});
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
	
	onUpdate(eventWebsite, updatedEventWebsite) {
		client({
			method: 'PUT',
			path: eventWebsite.entity._links.self.href,
			entity: updatedEventWebsite,
			headers: {
				'Content-Type': 'application/json',
				'If-Match': eventWebsite.headers.Etag
			}
		}).done(response => {
			this.loadFromServer(this.state.pageSize);
		}, response => {
			if (response.status.code === 403) {
				alert('ACCESS DENIED: You are not authorized to update ' +
						eventWebsite.entity._links.self.href);
			}
			if (response.status.code === 412) {
				alert('DENIED: Unable to update ' +
					eventWebsite.entity._links.self.href + '. Your copy is stale.');
			}
		});
	}

	// tag::delete[]
	onDelete(eventWebsite) {
		client({method: 'DELETE', path: eventWebsite.entity._links.self.href}).done(response => {
			this.loadFromServer(this.state.pageSize);},
			response => {
				if (response.status.code === 403) {
					alert('ACCESS DENIED: You are not authorized to delete ' +
						employee.entity._links.self.href);
				}
			}
		);
	}
	// end::delete[]

	// tag::navigate[]
	onNavigate(navUri) {
		client({
			method: 'GET', 
			path: navUri
		}).then(websiteCollection => {
			this.links = websiteCollection.entity._links;
			return websiteCollection.entity._embedded.eventWebsites.map(website =>
					client({
						method: 'GET',
						path: website._links.self.href
					})
			);
		}).then(websitePromises => {
			return when.all(websitePromises);
		}).done(eventWebsites => {
			this.setState({
				eventWebsites: eventWebsites,
				attributes: this.state.attributes,
				pageSize: this.state.pageSize,
				links: this.links
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
  			'eventWebsites', 'search', {rel: 'findByUriLike', params: {'uri': terms, 'size': this.state.pageSize}}]
		).then(websiteCollection => {
			this.links = websiteCollection.entity._links;
  			return websiteCollection.entity._embedded.eventWebsites.map(website =>
  					client({
  						method: 'GET',
  						path: website._links.self.href
  					})
  			);
  		}).then(websitePromises => {
  			return when.all(websitePromises);
  		}).done(eventWebsites => {
  			this.setState({
  				eventWebsites: eventWebsites,
  				attributes: this.schema,
  				pageSize: this.state.pageSize,
  				links: this.links});
  		});
	}
	
	connect(callback) {
        var socket = new SockJS('/hello');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function(greeting){
            	callback(JSON.parse(greeting.body).content);
            });
        });
    }
	
	showGreeting(message) {
        var response = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.appendChild(document.createTextNode(message));
        response.appendChild(p);
    }

    disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }
	

	// tag::follow-1[]
	componentDidMount() {
		this.disconnect();
		this.loadFromServer(this.state.pageSize);
		this.connect(this.showGreeting);
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
								  attributes={this.state.attributes}
								  onNavigate={this.onNavigate}
								  onUpdate={this.onUpdate}
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


class UpdateDialog extends React.Component {

	constructor(props) {
		super(props);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(e) {
		this.props.onUpdate(this.props.eventWebsite, e.formData);
		window.location = "#";
	}

	render() {
		var dialogId = "update-" + this.props.eventWebsite.entity._links.self.href;

		return (
			<div key={this.props.eventWebsite.entity._links.self.href}>
				<a href={"#" + dialogId}>Update</a>
				<div id={dialogId} className="modalDialog">
					<div>
						<a href="#" title="Close" className="close">X</a>

						<h2>Update an eventWebsite</h2>

						<Form schema={this.props.attributes} formData={this.props.eventWebsite.entity} onSubmit={this.handleSubmit}>
							<button>Update</button>
						</Form>
					</div>
				</div>
			</div>
		)
	}

};


class ValidateDialog extends React.Component {
	constructor(props) {
		super(props);
		this.send = this.send.bind(this);
		this.openNav = this.openNav.bind(this);
		this.closeNav = this.closeNav.bind(this);
	}
	
	send() {
        stompClient.send("/app/hello", {}, JSON.stringify({ 'eventWebsite': this.props.eventWebsite.entity }));
    }
	
	/* Open when someone clicks on the span element */
	openNav() {
	    document.getElementById("myNav").style.width = "100%";
	}

	/* Close when someone clicks on the "x" symbol inside the overlay */
	closeNav() {
	    document.getElementById("myNav").style.width = "0%";
	}
	
	render() {
		var dialogId = "validate-" + this.props.eventWebsite.entity._links.self.href;

		return (
			<div key={this.props.eventWebsite.entity._links.self.href}>
				<a href={"#" + dialogId} onClick={ () => {this.send(); this.openNav() }}>Validate</a>
				
				<div id="myNav" className="overlay">

				  <a href="javascript:void(0)" className="closebtn" onClick={this.closeNav}>&times;</a>

				  <div className="overlay-content">
				  	<p id="response"></p>
				  </div>
				</div>
				
			</div>
		)
	}
}

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
			// TODO: ugly!?
			this.refs.searchForm.reset();
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
			<EventWebsite attributes={this.props.attributes} 
							key={eventWebsite.entity._links.self.href} 
							eventWebsite={eventWebsite} 
							onUpdate={this.props.onUpdate} 
							onDelete={this.props.onDelete}/>
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
				<form ref="searchForm" onSubmit={this.handleSearch}>
					<input type="search" ref="search" />
					<input type="submit" value="Search" />
				</form>
				<label for="pageSize">Page size: </label>
				<input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput} size="2"/>
				<table>
					<tr>
						<th>Venue name</th>
						<th>URL</th>
						<th>Manager</th>
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
				<td>{this.props.eventWebsite.entity.uri}</td>
				<td>{this.props.eventWebsite.entity.manager.name}</td>
				<td>
					<UpdateDialog eventWebsite={this.props.eventWebsite}
								  attributes={this.props.attributes}
								  onUpdate={this.props.onUpdate}/>
				</td>
				<td>
					<ValidateDialog eventWebsite={this.props.eventWebsite}
								  attributes={this.props.attributes} />
				</td>	
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
