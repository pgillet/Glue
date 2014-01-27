var inputLocation;
var inputLat;
var inputLng;
var bounds;
var geocoder;
var autocompleteService;
var placesService;
// var autocomplete;

function initialize() {
	inputLocation = /** @type {HTMLInputElement} */
	document.getElementById("inputLocation");
	inputLat = document.getElementById("inputLat");
	inputLng = document.getElementById("inputLng");

	// SearchBox vs Autocomplete ?
	// autocomplete = new google.maps.places.Autocomplete(inputLocation);

	// Get autocomplete predictions programmatically from the autocomplete
	// service
	autocompleteService = new google.maps.places.AutocompleteService();

	var dummy = /** @type {HTMLDivElement} */
	document.createElement("dummy");
	placesService = new google.maps.places.PlacesService(dummy);

	if (inputLocation.value) {
		// Location already set
		return;
	}

	geocoder = new google.maps.Geocoder();

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successFunction, showError);
	} else {
		console.warn("Geolocation is not supported by this browser.");
	}
}

function getCurrentPosition() {
	// Reset location
	setPosition('', '', '');
	// Reinit
	initialize();
}

function setPosition(lat, lng, position) {
	inputLat.value = lat;
	inputLng.value = lng;
	inputLocation.value = position;
	console.log("lat = " + lat);
	console.log("lng = " + lng);
	console.log("position = " + position);
}

function showError(error) {
	switch (error.code) {
	case error.PERMISSION_DENIED:
		console.log("User denied the request for Geolocation.");
		break;
	case error.POSITION_UNAVAILABLE:
		console.warn("Location information is unavailable.");
		break;
	case error.TIMEOUT:
		console.warn("The request to get user location timed out.");
		break;
	case error.UNKNOWN_ERROR:
		console.error("An unknown error occurred.");
		break;
	}
}

// Get the latitude and the longitude;
function successFunction(position) {
	var lat = position.coords.latitude;
	var lng = position.coords.longitude;

	codeLatLng(lat, lng);

	// For later address suggestion
	configureAutocomplete(lat, lng);
}

// Configure the service to provide Place predictions within the area centered
// on the user location. Results are biased towards, but not restricted to, this
// area.
function configureAutocomplete(lat, lng) {
	var latLng = new google.maps.LatLng(lat, lng);
	var radius = 50000; // meters
	var circle = new google.maps.Circle({
		center : latLng,
		radius : radius
	});
	bounds = circle.getBounds();
	// autocomplete.setBounds(bounds);
}

function codeLatLng(lat, lng) {

	var latlng = new google.maps.LatLng(lat, lng);
	geocoder
			.geocode(
					{
						'location' : latlng
					},
					function(results, status) {
						if (status == google.maps.GeocoderStatus.OK) {
							// console.log(results);
							if (results[0]) {
								// formatted address
								// console.log(results[0].formatted_address);
								// find country name
								for ( var i = 0; i < results[0].address_components.length; i++) {
									for ( var b = 0; b < results[0].address_components[i].types.length; b++) {

										// there are different types that might
										// hold a city admin_area_lvl_1 usually
										// does in come cases looking for
										// sublocality type will be more
										// appropriate
										if (results[0].address_components[i].types[b] == "locality") {
											// this is the object you are
											// looking for
											city = results[0].address_components[i];
											break;
										}
									}
								}
								// city data
								setPosition(lat, lng, city.long_name);
								// console.log(city.short_name + " " +
								// city.long_name);

							} else {
								console.warn("No results found");
							}
						} else {
							console.error("Geocoder failed due to: " + status);
						}
					});
}

function predictLocation() {

	// Clean
	setPosition('', '', inputLocation.value);

	// TODO: call getQueryPredictions method instead of getPlacePredictions ?

	// TODO: add types property ?
	// types Array.<string> The types of predictions to be returned. Four types
	// are supported: 'establishment' for businesses, 'geocode' for addresses,
	// '(regions)' for administrative regions and '(cities)' for localities. If
	// nothing is specified, all types are returned.

	autocompleteService.getPlacePredictions({
		input : inputLocation.value,
		bounds : bounds,
		offset : 3
	}, autocompleteCallback);
}

function autocompleteCallback(predictions, status) {
	if (status != google.maps.places.PlacesServiceStatus.OK) {
		console.error(status);
		return;
	}

	// for ( var i = 0, prediction; prediction = predictions[i]; i++) {
	// console.log("Description = " + prediction.description);
	// }

	if (predictions[0]) {
		placesService.getDetails({
			reference : predictions[0].reference
		}, placesCallback);
	}
}

function placesCallback(placeResult, status) {
	if (status != google.maps.places.PlacesServiceStatus.OK) {
		console.error(status);
		return;
	}

	var latLng = placeResult.geometry.location;

	// We keep the user input
	setPosition(latLng.lat(), latLng.lng(), inputLocation.value);
}

google.maps.event.addDomListener(window, 'load', initialize);