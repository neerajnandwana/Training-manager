'use strict';

angular.module('myApp.services').factory('employeeService', ['commonService', function(commonService){
    var factory = {};
        
    factory.getEmployees = function(){
    	return commonService.getResource('emp');
    };
    
    factory.getEmployee = function(id){
    	return commonService.getResource('emp/'+id);
    };
    
    factory.destroyEmployee = function(id){
    	return commonService.destroyResource('emp/'+id);
    };

    return factory;
}]);