'use strict';

app.controllers.controller('FileCtrl', ['$scope', '$routeParams', '$location', '$timeout', 'fileService', function($scope, $routeParams, $location, $timeout, fileService) {
	var urlPath = '/file',
		fileId = $routeParams.id || '';
	
	function loadFiles (){
		fileService.getFiles().then(function(data){
			$scope.files = data.results;
		});		
	}
	
	function downloadFile(id){
		fileService.getFile(id).then(function(data){
			var file = data.results;
			file.$edit = true;
			$scope.file = file;
		});		
	}
	
	if (fileId){
		downloadFile(fileId);
	} else {
		loadFiles();
	}
	
	$scope.destroy = function(id){
		fileService.destroyFile(id).then(function(data){
			if($location.path() === urlPath){
				$timeout(loadFiles, 350);
			} else {
				$location.path(urlPath);
			}
		});
	};
	
	$scope.uploadFile = function(data){
		fileService.upload(data).then(function(data){
			$location.path(urlPath);
		});
	};
}]);