var app = angular.module('autocomplete',['ui.bootstrap']);
app.controller('AutoCompleteCtrl', function($scope, $http) {

  $scope.selected = undefined;

/*  $scope.getLocation = function(val) {
    $http.get('https://maps.googleapis.com/maps/api/place/autocomplete/json', {
      params: {
        input: val,
        key: 'AIzaSyAUpOmU7M1IQy6DCfFLB0YYENuNLDewI8M',
        types: '(cities)',
        components: 'country:fr'
      }
    })
    .then(function(response){
      return [{"formatted_address" : "Turin, Italie"}];
    })
    .catch(function(response) {
        console.error('error', response.status, response.data);
    })
    .finally(function() {
        console.log("finally");
    });
  };*/

    $scope.getLocation = function(val) {
    $http.get('//localhost:8080/glue/services/events/452', {
    })
    .then(function(response){
      return [{"formatted_address" : "Turin, Italie"}];
    })
    .catch(function(response) {
        console.error('error', response.status, response.data);
    })
    .finally(function() {
        console.log("finally");
    });
  };

/*    $scope.getLocation = function(val) {
    return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
      params: {
        address: val,
        sensor: false
      }
    }).then(function(response){
      return [{"toto" : "tata"}];
    });
  };*/

});