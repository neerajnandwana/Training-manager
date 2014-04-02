'use strict';

app.services.factory('employeeService', ['baseService', function(baseService){
    var factory = {};
        
    factory.getEmployees = baseService.getResourceFn('emp');    
    factory.getEmployee = baseService.getResourceFn('emp');    
    factory.destroyEmployee = baseService.destroyResourceFn('emp');
    factory.update = baseService.postResourceFn('emp');

    return factory;
}]);