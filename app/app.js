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
			})
			.when('/projects/:projectId/environments/:environmentId', {
				template: '<secret-list></secret-list>'
			});
	}]);