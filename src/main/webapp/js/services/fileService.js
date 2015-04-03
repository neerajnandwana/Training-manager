'use strict';

app.services.factory('fileService', ['$http', 'baseService', function($http, baseService){
    var factory = {},
    	baseResource = baseService.getServiceUrl("attachment");
        
    function processResult(results){
    	_.each(results, function(data){
    		data.link = baseResource + '/' + data.id + '/file'
    		data.size = Util.bytesToSize(data.size);
    	});
    };
    
    factory.getFiles = baseService.getResourceFn('attachment', processResult);    
    factory.getFile = baseService.getResourceFn('attachment');    
    factory.destroyFile = baseService.destroyResourceFn('attachment');
    
    factory.upload = function(file){
        var data = new FormData();
        data.append('file', file.file);
        data.append('desc', file.desc);
        return $http.post(baseResource, data, {
        	transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function (response) {
	        return response.data.result;
	    }, function (error) {
	    	console.log(error);
	    });
    };

    return factory;
}]);