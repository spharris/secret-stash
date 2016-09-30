angular
  .module('secretStash')
  .component('environmentList', {
    templateUrl: 'environment/environment-list.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        api.getList($routeParams.projectId).then(function(result) {
          $scope.environments = result.data.value;
        });
        
        api.getObject($routeParams.projectId).then(function(result) {
          $scope.project = result.data.value;
        });
      }
    ]
  });
