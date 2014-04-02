'use strict';

app.controllers.controller('TrainingCtrl', ['$scope', '$routeParams', '$location','$timeout', 'trainingService', function($scope, $routeParams, $location, $timeout, trainingService){
	var urlPath = '/training',
		trainingId = $routeParams.id || '';
	
	function loadTrainings(){
		trainingService.getTrainings().then(function(data){
			$scope.trainings = data.results;
		});			
	}
	
	function loadTraining(id){
		trainingService.getTraining(id).then(function(data){
			var training = data.results;
			training.$edit = true;
			$scope.training = training;	
		});
	}
	
	if (trainingId){
		loadTraining(trainingId);
	} else {
		loadTrainings();
	}
	
	$scope.destroy = function(id){
		trainingService.destroyTraining(id).then(function(data){
			if($location.path() === urlPath){
				$timeout(loadTrainings, 350);
			} else {
				$location.path(urlPath);
			}
		});
	};
	
	$scope.updateTraining = function(id, data){
		trainingService.update(id, data).then(function(data){
			$location.path(urlPath);
		});
	};
	
	$scope.addTraining = function(id, data){
		trainingService.update(id, data).then(function(data){
			$location.path(urlPath);
		});
	};
}]);