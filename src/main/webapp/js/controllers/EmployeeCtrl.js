'use strict';

angular.module('myApp.controllers').controller('EmployeeCtrl', ['$scope', '$routeParams', '$location', 'employeeService', function($scope, $routeParams, $location, employeeService) {
	var empId = $routeParams.id || '';
	
	$scope.destroy = function(id){
		employeeService.destroyEmployee(id).then(function(data){
			$location.path('/employee');
		});
	};
	
	if (empId){
		employeeService.getEmployee(empId).then(function(data){
			var employee = data.results;
			employee.$edit = true;
			$scope.employee = employee;
		});
	} else {
		employeeService.getEmployees().then(function(data){
			$scope.employees = data.results;
		});
	}
}]);