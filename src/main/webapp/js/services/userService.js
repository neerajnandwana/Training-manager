'use strict';

app.services.factory('userService', ['baseService', function(baseService){
    var factory = {};
        
    factory.getUsers = baseService.getResourceFn('user');    
    factory.getUser = baseService.getResourceFn('user');    
    factory.destroyUser = baseService.destroyResourceFn('user');
    factory.update = baseService.postResourceFn('user');

    return factory;
}]);