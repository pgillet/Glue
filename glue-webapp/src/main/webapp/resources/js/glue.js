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
	            for (var i = 0;  i < data.length;  i++) {
	                retval.push({
	                	value: data[i].title
	                });
	            }
	            return retval;
	        }
         },
         template: [
                    '<p>{{value}}</p>'
         ].join(''),
         engine: Hogan,
    })
    .on('typeahead:selected', function($e, datum){
        //alert(datum["value"]);
        document.getElementById("main_form:main_input").click();
        });
});