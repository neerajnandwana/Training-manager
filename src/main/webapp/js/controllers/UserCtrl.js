'use strict';

app.controllers.controller('UserCtrl', ['$scope', '$routeParams', '$location', '$timeout', 'userService', function($scope, $routeParams, $location, $timeout, userService){
	var urlPath = '/user',
		userId = $routeParams.id || '';
	
	function loadUsers(){
		userService.getUsers().then(function(data){
			$scope.users = data.results;
		});		
	}
	
	function loadUser(id){
		userService.getUser(id).then(function(data){
			var user = data.results;
			user.$edit = true;
			$scope.user = user;
		});		
	}
	
	if (userId){
		loadUser(userId);
	} else {
		loadUsers();
	}
	
	$scope.destroy = function(id){
		userService.destroyUser(id).then(function(data){
			if($location.path() === urlPath){
				$timeout(loadUsers, 350);
			} else {
				$location.path(urlPath);				
			}
		});
	};
	
	$scope.updateUser = function(id, data){
		userService.update(id, data).then(function(data){
			$location.path(urlPath);
		});
	};
}]);