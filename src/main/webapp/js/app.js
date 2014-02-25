'use strict';
var app = Util.namespace('app');
app.controllers = angular.module('myApp.controllers', []);
app.directives = angular.module('myApp.directives', []);
app.filters = angular.module('myApp.filters', []);
app.services = angular.module('myApp.services', []).value('version', '0.1');

// Declare app level module which depends on filters, and services
app.App = angular.module('myApp', [
	'ngRoute',
	'loadingBar',
	'mgcrea.ngStrap',
	'myApp.filters',
	'myApp.services',
	'myApp.directives',
	'myApp.controllers'
]);