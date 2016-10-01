angular
  .module('secretStash')
  .component('secretList', {
    templateUrl: 'secret/secret-list.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        api.getList($routeParams.projectId, $routeParams.environmentId).then(function(result) {
          $scope.secrets= result.data.value;
        });
        
        api.getObject($routeParams.projectId, $routeParams.environmentId).then(function(result) {
          $scope.environment = result.data.value;
        });
      }
    ]
  });
