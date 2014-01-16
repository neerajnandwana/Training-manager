'use strict';

angular.module('myApp.services').factory('commonService', ['$http','$q', function($http, $q){
    var serviceBase = '../r/',
	factory = {};

	factory.getResource = function (resource){
		return $http.get(serviceBase + resource).then(function (data) {
	        return {
	            results: data.data
	        };
	    }, function (error) {
	        console.log(arguments);
	    });
	};
	
	factory.destroyResource = function (resource){
	    return $http.delete(serviceBase + resource).then(function (results) {
	        return results.data;
	    }, function (error) {
	        alert(error.message);
	    });
	};
	
	factory.postResource = function (resource, data){
	    return $http.post(serviceBase + resource, data).then(function (results) {
	        return results.data;
	    });
	};
	
	return factory;
}]);