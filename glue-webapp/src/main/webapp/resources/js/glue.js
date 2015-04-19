// A function that returns an array of URL query parameters
// qs["param"] -> value
var qs = (function(a) {
    if (a == "") return {};
    var b = {};
    for (var i = 0; i < a.length; ++i)
    {
        var p=a[i].split('=', 2);
        if (p.length == 1)
            b[p[0]] = "";
        else
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
    }
    return b;
})(window.location.search.substr(1).split('&'));

function scrollToTop(data) {
	if (data.status == 'success') {
		window.scrollTo(0, 0);
	}
}

// instantiate the typeahead UI
$(document).ready(function() {
	
	//instantiate the bloodhound suggestion engine
	var events = new Bloodhound({
	    datumTokenizer: function (d) {
	        return Bloodhound.tokenizers.whitespace(d.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/glue/services/search/autocomplete?query=%QUERY',
	        replace: function () {
	        	var q = '/glue/services/search/autocomplete?query=' + $('.typeahead').val();
	        	var lat = document.getElementById("lat").value;
	        	var lng = document.getElementById("lng").value;
	        	if (lat && lng) {
	        		q = q + '&lat=' + lat + "&lng=" + lng;
	        	}
	        	return q;
	        },
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
    	return $("#main_form").submit();
        })
	.on('input.typeahead').keypress(function (e) {
        if (e.which == 13) {
        	return $("#main_form").submit();
        }
    });
});

$('#q').focus(function() {
	codeAddress0(undefined);
});

//Location field
$('#ql').keypress(function (e) {	
	if (e.which == 13) {
		e.preventDefault();
		codeAddress();
    }
});

$('#dummy-search-input').click(function (e) {
	codeAddress();
});
