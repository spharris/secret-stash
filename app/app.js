angular
	.module('secretStash', ['ngRoute', 'stashApi'])
	.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
	  $locationProvider.hashPrefix('!');

		$routeProvider
			.when('/', {
				template: '<project-list></project-list>'
			})
			.when('/projects/:projectId', {
				template: '<environment-list></environment-list>'
			});
	}]);