'use strict';

angular.module('responseErrorHandler',[]).
	config(['$httpProvider', function($httpProvider){
		$httpProvider.interceptors.push(['$q', '$timeout', '$rootScope', function($q, $timeout, $rootScope) {

			function loadingComplete(error){
				$rootScope.loadingBarMessage = error;	
				endTimeout = $timeout(function() {
					$rootScope.loadingBar = false;
				}, 3000);
			}
			
			return {
				request : function(config) {
					return config || $q.when(config);
				},
		
				requestError : function(rejection) {
					return $q.reject(rejection);
				},
		
				response : function(response) {
					return response || $q.when(response);
				},
		
				responseError : function(rejection) {
					return $q.reject(rejection);
				}
			};
		}]);
	}]);