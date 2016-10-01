angular
  .module('secretStash')
  .component('secretCreateForm', {
    templateUrl: 'secret/secret-create-form.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        var self = this;
        
        /* Functions */
        self.createSecret = function() {
          api.putObject(self.secret, $routeParams.projectId, $routeParams.environmentId).then(
              function(result) {
                self.onCreate();
              }
          );
        };
      }
    ],
    bindings: {
      modalId: '@',
      onCreate: '&'
    }
  });
