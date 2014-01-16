'use strict';

angular.module('myApp.controllers').controller('UserCtrl', ['$scope', '$routeParams', '$location', 'userService', function($scope, $routeParams, $location, userService){
	var userId = $routeParams.id || '';
	
	$scope.destroy = function(id){
		userService.destroyUser(id).then(function(data){
			$location.path('/user');
		});
	};
	if (userId){
		userService.getUser(userId).then(function(data){
			var user = data.results;
			user.$edit = true;
			$scope.user = user;
		});
	} else {
		userService.getUsers().then(function(data){
			$scope.users = data.results;
		});
	}
}]);