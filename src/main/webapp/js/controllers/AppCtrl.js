'use strict';

angular.module('myApp.controllers').controller('AppCtrl', ['$scope', '$rootScope', function($scope, $rootScope){
	$rootScope.empLevels = [ 
		{name: 'C-1', value: 'c1'},
		{name: 'C-2', value: 'c2'},
		{name: 'C-3', value: 'c3'},
		{name: 'C-4', value: 'c4'},
		{name: 'C-5', value: 'c5'},
		{name: 'C-6', value: 'c6'},
		{name: 'C-7', value: 'c7'} ];
	$rootScope.trainingKinds = ['Analysis','Business Awareness','Finance','Soft Skills','System and Tools'];
	$rootScope.trainingModes = ['Class Room','E Learning','Vertual'];
}]);