angular
  .module('secretStash')
  .component('secretList', {
    templateUrl: 'secret/secret-list.html',
    controller: ['$scope', '$routeParams', '$window', 'stashApiService', 
      function($scope, $routeParams, $window, api) {
        var self = this;

        self.projectId = $routeParams.projectId;
        self.environment = null;
        self.secrets = [];

        var init = function() {
          api.getObject($routeParams.projectId, $routeParams.environmentId).then(function(result) {
            $scope.environment = result.data.value;
          });
          
          api.getObject($routeParams.projectId).then(function(result) {
            $scope.project = result.data.value;
          });

          self.updateSecretList();
        }
        
        /* Functions */
        self.updateSecretList = function() {
          api.getList($routeParams.projectId, $routeParams.environmentId).then(function(result) {
            self.secrets = result.data.value;
          });
        }
        
        self.deleteSecret = function(secretId) {
          if ($window.confirm('Really delete ' + secretId + '?')) {
            api.deleteObject($routeParams.projectId, $routeParams.environmentId, secretId)
              .then(self.updateSecretList);
          }
        }

        init();
      }
    ]
  });
