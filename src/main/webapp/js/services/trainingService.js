'use strict';

angular.module('myApp.services').factory('trainingService', ['commonService', function(commonService){
    var factory = {};
        
    factory.getTrainings = function(){
    	return commonService.getResource('training');
    };
    
    factory.getTraining = function(id){
    	return commonService.getResource('training/'+id);
    };
    
    factory.destroyTraining = function(id){
    	return commonService.destroyResource('training/'+id);
    };

    return factory;
}]);