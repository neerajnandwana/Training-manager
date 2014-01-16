'use strict';

angular.module('myApp.controllers', []);
angular.module('myApp.directives', []);
angular.module('myApp.filters', []);
angular.module('myApp.services', []).value('version', '0.1');

// Declare app level module which depends on filters, and services
angular.module('myApp', [
	'ngRoute',
	'myApp.filters',
	'myApp.services',
	'myApp.directives',
	'myApp.controllers'
]);