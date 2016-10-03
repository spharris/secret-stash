angular
  .module('secretStash')
  .component('environmentList', {
    templateUrl: 'environment/environment-list.html',
    controller: ['$scope', '$routeParams', '$window', 'stashApiService', 
      function($scope, $routeParams, $window, api) {
        var self = this;
        
        self.project = null;
        
        var init = function() {
          self.updateEnvironmentList();
          
          api.getObject($routeParams.projectId).then(function(result) {
            self.project = result.data.value;
          });
        }

        self.updateEnvironmentList = function() {
          api.getList($routeParams.projectId).then(function(result) {
            $scope.environments = result.data.value;
          });
        }
        
        self.deleteEnvironment = function(environmentId) {
          if ($window.confirm('Really delete ' + environmentId + '?')) {
            api.deleteObject($routeParams.projectId, environmentId)
              .then(self.updateEnvironmentList);
          }
        }
        
        init();
      }
    ]
  });
