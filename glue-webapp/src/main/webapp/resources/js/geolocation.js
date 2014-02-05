var inputLocation;
var inputLat;
var inputLng;
var bounds;
var geocoder;
// var autocomplete;

function initialize() {
	inputLocation = /** @type {HTMLInputElement} */
	document.getElementById("inputLocation");
	inputLat = document.getElementById("inputLat");
	inputLng = document.getElementById("inputLng");

	// SearchBox vs Autocomplete ?
	// autocomplete = new google.maps.places.Autocomplete(inputLocation);

	geocoder = new google.maps.Geocoder();

	if (inputLocation.value) {
		// Location already set
		return;
	}

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successFunction, showError);
	} else {
		console.warn("Geolocation is not supported by this browser.");
	}
}

function getCurrentPosition() {
	// Clear position
	clearPosition();
	// Reinit
	initialize();
}

function locationResult(location, lat, lng) {
	this.location = location;
	this.lat = lat;
	this.lng = lng;
}

function setPosition(locationResult) {
	// Set the new position
	inputLocation.value = locationResult.location;
	if (locationResult.lat && locationResult.lng) {
		inputLat.value = locationResult.lat;
		inputLng.value = locationResult.lng;
	}

	console.log("lat = " + locationResult.lat + ", long = "
			+ locationResult.lng + ", city = " + locationResult.location);
}

function clearPosition() {
	// Clear
	inputLocation.value = inputLat.value = inputLng.value = '';
}

function showError(error) {
	switch (error.code) {
	case error.PERMISSION_DENIED:
		console.log("User denied the request for Geolocation.");
		var btn = document.getElementById("btnLocation");
		btn.title = "Glue has been blocked from tracking your location. Clear your settings to re-enable the user location.";
		// btn.disabled = "disabled";
		btn.innerHTML = "<span class=\"glyphicon glyphicon-warning-sign text-muted\"></span>";
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
								var position = new locationResult(
										city.long_name, lat, lng);
								setPosition(position);
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

function codeAddress() {
	codeAddress0(function() {
		// TODO: awful! We are not supposed to know the underlying JSF implementation...
		return myfaces.oam.submitForm('main_form','search_input');
	});
}

function codeAddress0(callbackFunc) {
	// Reset lat/lng
	inputLat.value = inputLng.value = '';

	if (inputLocation.value) {
		var address = inputLocation.value;

		geocoder.geocode({
			'address' : address,
			'bounds' : bounds
		}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				if (results[0]) {
					// city data
					console.log("Address found = "
							+ results[0].formatted_address);
					var location = results[0].geometry.location;
					var position = new locationResult(address, location.lat(),
							location.lng());
					setPosition(position);

				} else {
					console.warn("No results found");
				}
			} else {
				console.error("Geocoder failed due to: " + status);
			}

			// Callback
			if (callbackFunc) {
				callbackFunc();
			}
		});
	}
}

google.maps.event.addDomListener(window, 'load', initialize);