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
	