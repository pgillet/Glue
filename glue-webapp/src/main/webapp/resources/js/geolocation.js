var x;
var geocoder;

function getLocation() {
	x = document.getElementById("location");
	geocoder = new google.maps.Geocoder();

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successFunction, showError);
	} else {
		x.innerHTML = "Geolocation is not supported by this browser.";
	}
}

function showPosition(position) {
	x.innerHTML = "<span class=\"glyphicon glyphicon-map-marker\"></span> "
			+ position;
}

function showError(error) {
	switch (error.code) {
	case error.PERMISSION_DENIED:
		x.innerHTML = "User denied the request for Geolocation.";
		break;
	case error.POSITION_UNAVAILABLE:
		x.innerHTML = "Location information is unavailable.";
		break;
	case error.TIMEOUT:
		x.innerHTML = "The request to get user location timed out.";
		break;
	case error.UNKNOWN_ERROR:
		x.innerHTML = "An unknown error occurred.";
		break;
	}
}

// Get the latitude and the longitude;
function successFunction(position) {
	var lat = position.coords.latitude;
	var lng = position.coords.longitude;
	codeLatLng(lat, lng);
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
							console.log(results);
							if (results[1]) {
								// formatted address
								// alert(results[0].formatted_address);
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
								showPosition(city.long_name);
								// alert(city.short_name + " " +
								// city.long_name);

							} else {
								alert("No results found");
							}
						} else {
							alert("Geocoder failed due to: " + status);
						}
					});
}