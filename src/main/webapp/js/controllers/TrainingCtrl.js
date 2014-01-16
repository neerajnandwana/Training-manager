'use strict';

angular.module('myApp.controllers').controller('TrainingCtrl', ['$scope', '$routeParams', '$location', 'trainingService', function($scope, $routeParams, $location, trainingService){
	var trainingId = $routeParams.id || '';
	
	$scope.destroy = function(id){
		trainingService.destroyTraining(id).then(function(data){
			$location.path('/training');
		});
	};
	
	if (trainingId){
		trainingService.getTraining(trainingId).then(function(data){
			var training = data.results;
			training.$edit = true;
			$scope.training = training;	
		});
	} else {
		trainingService.getTrainings().then(function(data){
			$scope.trainings = data.results;
		});	
	}
}]);