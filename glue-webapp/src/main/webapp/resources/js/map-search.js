var oldBounds;
var LeafIcon;
var catIconMap;
var markers;
var zoomThreshold = 13;

initialize();

function initialize() {
	LeafIcon = L.Icon.extend({
		options : {
			shadowUrl : '../resources/img/markers/shadow-marker.png',
			iconSize : [ 21, 34 ],
			shadowSize : [ 40, 37 ],
			iconAnchor : [ 10, 34 ],
			shadowAnchor : [ 12, 35 ],
			popupAnchor : [ 0, -20 ]
		}
	});
	
	catIconMap = {};
	catIconMap['MUSIC'] = ['fa', 'music' , '#3a87ad', ''];
	catIconMap['PERFORMING_ART'] = ['', 'theatre' , '#b94a48', 'dark maki-icon'];
	catIconMap['EXHIBITION'] = ['', 'art-gallery' , '#ef7c00', 'dark maki-icon'];
	catIconMap['SPORT'] = ['fa', 'fa-futbol-o' , '#468847', ''];
	catIconMap['YOUTH'] = ['fa', 'child' , '#5bc0de', ''];
	catIconMap['OTHER'] = ['fa', 'cloud' , '#bd1789', ''];
	catIconMap['CONFERENCE'] = ['fa', 'university' , '#c09853', ''];

	var osmAttrib = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';

	// MQ layer
	var mqUrl = 'http://{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png';
	domains = [ 'otile1', 'otile2', 'otile3', 'otile4' ];
	var mqAttrib = 'Tiles &copy; <a href="http://www.mapquest.com/" target="_blank">MapQuest</a> <img src="http://developer.mapquest.com/content/osm/mq_logo.png" />';
	var mqLayer = L.tileLayer(mqUrl, {
		maxZoom : 18,
		subdomains : domains,
		attribution : osmAttrib + ', ' + mqAttrib
	});

	// Setting up the map
	var map = L.map('glue-map', {
		center : [ qs["lat"], qs["lng"] ],
		zoom : zoomThreshold,
		zoomControl : false,
		layers : [ mqLayer ]
	});

	// Zoom control position
	var control = L.control.zoom({
		position : 'bottomright'
	});
	control.addTo(map);

	markers = L.markerClusterGroup();
	map.addLayer(markers);

	map.on('dragend zoomend', onChange);
	// init
	search(map.getBounds());

	oldBounds = map.getBounds();
}

function onChange(e) {

	// target is the map that fired the event.
	var map = e.target;
	var latLngBounds = map.getBounds();

	if (map.getZoom() >= zoomThreshold && !oldBounds.contains(latLngBounds)) {
		search(latLngBounds);
	}
}

function search(latLngBounds) {
	
	console.log("Context path = " + _contextPath);

	var xmlhttp = new XMLHttpRequest();
	var url = _contextPath + "/services/search/events";
	var params = window.location.search + "&bbox="
			+ latLngBounds.toBBoxString() + "&rows=1000";

	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var myArr = JSON.parse(xmlhttp.responseText);
			putMarkers(myArr);
		}
	}
	xmlhttp.open("GET", url + params, true);
	xmlhttp.send();

	oldBounds = latLngBounds;
}

function putMarkers(arr) {
	markers.clearLayers();
	// map.removeLayer(markers);

	var markersArray = new Array(arr.length);

	for (var i = 0; i < arr.length; i++) {
		event = arr[i];
		
		var iconProps = catIconMap[event.category];
		
		catIcon = L.VectorMarkers.icon({
			prefix: iconProps[0],
		    icon: iconProps[1],
		    markerColor: iconProps[2],
		    extraClasses: iconProps[3]
		});
		
		marker = L.marker([ event.venue.latitude, event.venue.longitude ], {
			icon : catIcon
		});
		eventurl = "/glue/stream/item.xhtml?id=" + event.id;
		eventlink = "<strong><a href=" + eventurl + ">" + event.title
				+ "</a></strong>";
		venueurl = "/glue/venues/search.xhtml?v=" + event.venue.id;
		venuelink = '<small><a href=' + venueurl + ' class="glue-a">'
				+ event.venue.name + '</a></small>';
		content = eventlink + '<br/>' + venuelink;
		marker.bindPopup(content);
		marker.on('mouseover', function(e) {
			this.openPopup();
		});
		markersArray[i] = marker;
	}

	markers.addLayers(markersArray);
	// map.addLayer(markers);
}
