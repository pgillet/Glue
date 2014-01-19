function scrollToTop(data) {
    if (data.status == 'success') {
    	window.scrollTo(0,0);
    }
}

$(document).ready(function() {
    $('.typeahead').typeahead({                               
         name: "cities",                
         remote: 'http://localhost:8080/glue/autocomplete?query=%QUERY'
         // TODO: to be configurable
         //remote: 'http://54.218.127.196:8080/glue/autocomplete?query=%QUERY'
    });
});