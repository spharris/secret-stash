angular
  .module('secretStash')
  .component('environmentCreateForm', {
    templateUrl: 'environment/environment-create-form.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        var self = this;
        
        /* Functions */
        self.createEnvironment = function() {
          api.putObject(self.environment, $routeParams.projectId).then(
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
