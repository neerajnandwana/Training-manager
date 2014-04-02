'use strict';

app.services.factory('trainingService', ['baseService', function(baseService){
    var factory = {};
        
    factory.getTrainings = baseService.getResourceFn('training');    
    factory.getTraining = baseService.getResourceFn('training');    
    factory.destroyTraining = baseService.destroyResourceFn('training');
    factory.update = baseService.postResourceFn('training');

    return factory;
}]);