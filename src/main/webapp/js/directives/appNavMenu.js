'use strict';

angular.module('myApp.directives').directive( 'appNavMenu', function($location) {
	return function(scope, element, attrs) {
		var links = element.find('li'), 
			onClass = attrs.appNavMenu || 'on', 
			urlMap = {}, 
			routePattern, link, url, currentLink, i;
	
		if (!$location.$$html5) {
			routePattern = /^#[^/]*/;
		}
	
		for (i = 0; i < links.length; i++) {
			link = angular.element(links[i]);
			url = angular.element(link.find('a')[0]).attr('href');
	
			if ($location.$$html5) {
				urlMap[url] = link;
			} else {
				urlMap[url.replace(routePattern, '')] = link;
			}
		}
	
		scope.$on('$routeChangeStart', function() {
			var pathLink = urlMap[$location.path()];
	
			if (pathLink) {
				if (currentLink) {
					currentLink.removeClass(onClass);
				}
				currentLink = pathLink;
				currentLink.addClass(onClass);
			}
		});
	};
});