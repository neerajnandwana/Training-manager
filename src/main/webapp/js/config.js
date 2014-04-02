'use strict';

app.App.config(['$provide', '$routeProvider', '$datepickerProvider', function($provide, $routeProvider, $datepickerProvider) {
	
	var userProfile = angular.copy(window.up);
	
	//profile as a injectible constant
	$provide.constant('userProfile', userProfile);
	
	$routeProvider.
		when('/home',   			{templateUrl: 'partials/home.html',				controller: 'HomeCtrl'}).
		when('/training',			{templateUrl: 'partials/training.html', 		controller: 'TrainingCtrl'}).
		when('/training/new',		{templateUrl: 'partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/training/view/:id',	{templateUrl: 'partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/training/edit/:id',	{templateUrl: 'partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/employee', 			{templateUrl: 'partials/employee.html', 		controller: 'EmployeeCtrl'}).
		when('/employee/new', 		{templateUrl: 'partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/employee/view/:id', 	{templateUrl: 'partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/employee/edit/:id', 	{templateUrl: 'partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/user',	  			{templateUrl: 'partials/user.html', 			controller: 'UserCtrl'}).
		when('/user/new',	  		{templateUrl: 'partials/user-detail.html', 		controller: 'UserCtrl'}).
		when('/user/view/:id',	  	{templateUrl: 'partials/user-detail.html', 		controller: 'UserCtrl'}).
		when('/user/edit/:id',	  	{templateUrl: 'partials/user-detail.html', 		controller: 'UserCtrl'}).
		otherwise({redirectTo: '/home'});
	
	angular.extend($datepickerProvider.defaults, {
		dateFormat: 'dd/MM/yyyy',
		startWeek: 1
	});
	
	
}]);