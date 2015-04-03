'use strict';

app.directives.directive("filereadModel", [ function() {
	return {
		scope : {
			filereadModel : "="
		},
		link : function(scope, element, attributes) {
			element.bind("change", function(changeEvent) {
				scope.$apply(function() {
					scope.filereadModel = changeEvent.target.files[0];
					// or all selected files:
					// scope.fileread = changeEvent.target.files;
				});
			});
		}
	};
}]);