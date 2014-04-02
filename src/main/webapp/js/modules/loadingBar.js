'use strict';

angular.module('loadingBar',[]).
	config(['$httpProvider', function($httpProvider){
		$httpProvider.interceptors.push(['$q', '$timeout', '$rootScope', function($q, $timeout, $rootScope) {
			var requests =  0,
				threshold = 0,
				endTimeout;
			
			function loadingComplete(){
				requests--;
				if(requests <= 0){
					$timeout.cancel(endTimeout);
					requests = 0;
					endTimeout = $timeout(function() {
						$rootScope.loadingBar = false;
					}, 500);
				}
			}
			
			$rootScope.loadingBarMessage = 'Loading...';
			return {
				request : function(config) {
					if (requests === 0) {
						$timeout.cancel(endTimeout);
						$rootScope.loadingBar = true;
					}
					requests++;
					return config || $q.when(config);
				},
		
				requestError : function(rejection) {
					loadingComplete();
					return $q.reject(rejection);
				},
		
				response : function(response) {
					loadingComplete();
					return response || $q.when(response);
				},
		
				responseError : function(rejection) {
					loadingComplete();
					return $q.reject(rejection);
				}
			};
		}]);
	}]);