angular
  .module('secretStash')
  .component('secretList', {
    templateUrl: 'secret/secret-list.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        var self = this;

        self.environment = null;
        self.secrets = [];

        var init = function() {
          api.getObject($routeParams.projectId, $routeParams.environmentId).then(function(result) {
            $scope.environment = result.data.value;
          });

          self.updateSecretList();
        }
        
        /* Functions */
        self.updateSecretList = function() {
          api.getList($routeParams.projectId, $routeParams.environmentId).then(function(result) {
            self.secrets = result.data.value;
          });
        }
        
        self.createSecret = function() {
          api.putObject(self.secret, $routeParams.projectId, $routeParams.environmentId).then(
              function(result) {
                self.secrets.unshift(result.data.value);
                self.secret = {}
              }
          );
          
          self.secretForm.$setPristine();
        };
        
        self.deleteSecret = function(secretId) {
          api.deleteObject($routeParams.projectId, $routeParams.environmentId, secretId)
            .then(self.updateSecretList);
        }

        init();
      }
    ]
  });
