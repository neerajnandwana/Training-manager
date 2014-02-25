'use strict';

app.services.factory('baseService', ['$http', '$q', function($http, $q){
    var serviceBase = '../r/',
		factory = {};

    function normalizeSubresource(res){
    	return res ? '/'+res : '';
    }
    
	factory.getResourceFn = function (resource){
		return function(subresource){
			var service = serviceBase + resource + normalizeSubresource(subresource);
			return $http.get(service).then(function (response) {
		        return { results: response.data.result };
		    }, function (error) {
		        console.log(error);
		    });			
		};
	};
	
	factory.destroyResourceFn = function (resource){
		return function(subresource){
			var service = serviceBase + resource + normalizeSubresource(subresource);
		    return $http.delete(service).then(function (response) {
		        return response.data.result;
		    }, function (error) {
		    	console.log(error);
		    });
		};
	};
	
	factory.postResourceFn = function (resource){
		return function(subresource, data){
			var service = serviceBase + resource + normalizeSubresource(subresource);
		    return $http.post(service, data).then(function (response) {
		        return response.data.result;
		    });			
		};
	};
	
	return factory;
}]);