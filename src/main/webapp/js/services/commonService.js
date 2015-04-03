'use strict';

app.services.factory('baseService', ['$http', '$q', function($http, $q){
    var serviceBase = '../sr/',
		factory = {};

    function normalizeSubresource(res){
    	return res ? '/'+res : '';
    }
    
    factory.getServiceUrl = function(resource, subresource){
    	return serviceBase + resource + normalizeSubresource(subresource);
    };
    
	factory.getResourceFn = function (resource, transformFn){
		return function(subresource){
			var service = serviceBase + resource + normalizeSubresource(subresource);
			return $http.get(service).then(function (response) {
				( transformFn || _.noop)(response.data.result);
		        return { results:  response.data.result};
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