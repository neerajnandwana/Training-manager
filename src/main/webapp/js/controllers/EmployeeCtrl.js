'use strict';

app.controllers.controller('EmployeeCtrl', ['$scope', '$routeParams', '$location', '$timeout', 'employeeService', function($scope, $routeParams, $location, $timeout, employeeService) {
	var urlPath = '/employee',
		empId = $routeParams.id || '';
	
	function loadEmployees (){
		employeeService.getEmployees().then(function(data){
			$scope.employees = data.results;
		});		
	}
	
	function loadEmployee(id){
		employeeService.getEmployee(id).then(function(data){
			var employee = data.results;
			employee.$edit = true;
			$scope.employee = employee;
		});		
	}
	
	if (empId){
		loadEmployee(empId);
	} else {
		loadEmployees();
	}
	
	$scope.destroy = function(id){
		employeeService.destroyEmployee(id).then(function(data){
			if($location.path() === urlPath){
				$timeout(loadEmployees, 350);
			} else {
				$location.path(urlPath);
			}
		});
	};
	
	$scope.updateEmployee = function(id, data){
		employeeService.update(id, data).then(function(data){
			$location.path(urlPath);
		});
	};
}]);