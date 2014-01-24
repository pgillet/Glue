function scrollToTop(data) {
    if (data.status == 'success') {
    	window.scrollTo(0,0);
    }
}

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
        //alert(datum["value"]);
        document.getElementById("main_form:main_input").click();
        });
});