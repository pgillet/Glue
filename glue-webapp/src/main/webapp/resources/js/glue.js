function scrollToTop(data) {
    if (data.status == 'success') {
    	window.scrollTo(0,0);
    }
}


//instantiate the bloodhound suggestion engine
var events = new Bloodhound({
    datumTokenizer: function (d) {
        return Bloodhound.tokenizers.whitespace(d.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/glue/services/autocomplete?query=%QUERY',
        filter: function (events) {
	        retval = [];	        	
	        for (var i = 1;  i < events.length;  i++) {
	            retval.push({
	            value: events[0] + events[i],
	            query: events[0],
	            comp: events[i]
	            });
	        }
	        return retval;
        }
    }
});

// initialize the bloodhound suggestion engine
events.initialize();

// instantiate the typeahead UI
$(document).ready(function() {
	$('.typeahead')
	.typeahead({
		hint: false,
		minLength: 2
		}, {
	    displayKey: 'value',
	    source: events.ttAdapter(),
	    templates: {
	    	suggestion: Handlebars.compile([
	    	                                '<p>{{query}}<b>{{comp}}</b></p>'	    	                                
	    	                              ].join(''))
	      }
	})
	.on('typeahead:selected', function($e, datum){
    	return myfaces.oam.submitForm('main_form','search_input');
        })
	.on('input.typeahead').keypress(function (e) {
        if (e.which == 13) {
        	return myfaces.oam.submitForm('main_form','search_input');
        }
    });
});

//Location field
$('#ql').on('input.ql').keypress(function (e) {
	if (e.which == 13) {
		 codeAddress();
    }
});

$('#ql').on('input.ql').change(function (e) {
	codeAddress0(undefined);
});


function createmap(events) {
	var map = L.map('map').setView([43.5882114,1.4436048], 12);
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
	attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
	}).addTo(map);
	L.marker([43.610228,1.445682]).addTo(map); 
	$.each(events, function(index) {
		if (events[index].venue.parent != null) {
			L.marker([events[index].venue.parent.latitude, events[index].venue.parent.longitude]).addTo(map)
			.bindPopup('<strong>'+events[index].title+'</strong><br>@ '+events[index].venue.name);
		} 		
		// add a marker in the given location
		//L.marker([events[index].venue.parent.latitude, events[index].venue.parent.longitude]).addTo(map);        
    });
}

