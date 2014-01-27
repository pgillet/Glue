function scrollToTop(data) {
    if (data.status == 'success') {
    	window.scrollTo(0,0);
    }
}

// Twitter typehead
$(document).ready(function() {
    $('.typeahead')
    .typeahead({                               
         name: "autocomplete",
         remote: {
        	url: '/glue/services/autocomplete?query=%QUERY',   
        	// need a "value" field ...
	        filter: function(data) {
	        	retval = [];	        	
	            for (var i = 1;  i < data.length;  i++) {
	                retval.push({
	                	value: data[0] + data[i],
	                	query: data[0],
	                	comp: data[i]
	                });
	            }
	            return retval;
	        }
         },
         template: [
                    '<p>{{query}}<b>{{comp}}</b></p>'
         ].join(''),
         engine: Hogan,
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
$('#inputLocation').on('input.inputLocation').keypress(function (e) {
	predictLocation();
	if (e.which == 13) {
    	return myfaces.oam.submitForm('main_form','search_input');
    }
});
	