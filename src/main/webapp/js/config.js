'use strict';

app.App.config(['$provide', '$routeProvider', '$datepickerProvider', function($provide, $routeProvider, $datepickerProvider) {
	
	var userProfile = angular.copy(window.up);
	
	//profile as a injectible constant
	$provide.constant('userProfile', userProfile);
	
	$routeProvider.
		when('/home',   			{templateUrl: '../s/partials/home.html',				controller: 'HomeCtrl'}).
		when('/training',			{templateUrl: '../s/partials/training.html', 			controller: 'TrainingCtrl'}).
		when('/training/new',		{templateUrl: '../s/partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/training/view/:id',	{templateUrl: '../s/partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/training/edit/:id',	{templateUrl: '../s/partials/training-detail.html', 	controller: 'TrainingCtrl'}).
		when('/employee', 			{templateUrl: '../s/partials/employee.html', 			controller: 'EmployeeCtrl'}).
		when('/employee/new', 		{templateUrl: '../s/partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/employee/view/:id', 	{templateUrl: '../s/partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/employee/edit/:id', 	{templateUrl: '../s/partials/employee-detail.html', 	controller: 'EmployeeCtrl'}).
		when('/user',	  			{templateUrl: '../s/partials/user.html', 				controller: 'UserCtrl'}).
		when('/user/new',	  		{templateUrl: '../s/partials/user-detail.html', 		controller: 'UserCtrl'}).
		when('/user/view/:id',	  	{templateUrl: '../s/partials/user-detail.html', 		controller: 'UserCtrl'}).
		when('/user/edit/:id',	  	{templateUrl: '../s/partials/user-detail.html', 		controller: 'UserCtrl'}).
		when('/file',	  			{templateUrl: '../s/partials/file.html', 				controller: 'FileCtrl'}).
		when('/file/new',	  		{templateUrl: '../s/partials/file-upload.html', 		controller: 'FileCtrl'}).
		otherwise({redirectTo: '/home'});
	
	angular.extend($datepickerProvider.defaults, {
		dateFormat: 'dd/MM/yyyy',
		startWeek: 1
	});
	
	
}]);