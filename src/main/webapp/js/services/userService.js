'use strict';

angular.module('myApp.services').factory('userService', ['commonService', function(commonService){
    var factory = {};
        
    factory.getUsers = function(){
    	return commonService.getResource('user');
    };
    
    factory.getUser = function(id){
    	return commonService.getResource('user/'+id);
    };
    
    factory.destroyUser = function(id){
    	return commonService.destroyResource('user/'+id);
    };

    return factory;
}]);