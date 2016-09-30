var app = angular.module('secretStash', [])

app.controller('ProjectListController', function($scope, $http) {
	$http.get('api/projects').then(function(response) {
		$scope.projects = response.data.value;
	});
});