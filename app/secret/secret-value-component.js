angular
  .module('secretStash')
  .component('secretValue', {
    templateUrl: 'secret/secret-value.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        var self = this;
        
        self.secretValue = null;
        
        self.showValue = function() {
          api.getObject($routeParams.projectId, $routeParams.environmentId, self.secretId,
              {includeSecretValue: true})
            .then(function(result) {
              self.secretValue = result.data.value.secretValue;
            });
        }
        
        self.hideValue = function() {
          self.secretValue = null;
        }
      }
    ],
    bindings: {
      secretId: '@'
    }
  });
